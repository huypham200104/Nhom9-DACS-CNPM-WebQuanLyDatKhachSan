package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.LoginDto;
import com.example.QuanLyKhachSan.enums.Role;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.repository.UserRepository;
import com.example.QuanLyKhachSan.response.AuthenticationResponse;
import com.example.QuanLyKhachSan.service.AuthenticationService;
import com.example.QuanLyKhachSan.service.TokenBlacklistService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest; // Import HttpServletRequest
import jakarta.servlet.http.HttpSession;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.QuanLyKhachSan.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @NonFinal
    protected static final String SIGNER_KEY = "wBGLhuHLapPvoba5VsZX1FPR6VM8DhJ337+DJGZwRpD4R2rxn5BGw9gnT+zKd3rW";

    @Override
    public AuthenticationResponse authenticate(LoginDto loginDto) throws ParseException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean authenticated = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new RuntimeException("Invalid credentials");
        }

        var token = generateToken(user);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        long expiresIn = (expiryTime.getTime() - System.currentTimeMillis()) / 1000;


        // Trả về thông tin để frontend lưu vào session storage
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .expiresIn(expiresIn)
                .userId(user.getUserId())
                .tokenType("Bearer")
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }



    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("testbookingdi.com")
                .issueTime(new java.util.Date())
                .expirationTime(new java.util.Date(
                        Instant.now().plus(1, java.time.temporal.ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("role", user.getRole())
                .claim("email", user.getEmail())
                .claim("userId", user.getUserId())
                .build();

        Payload payload = new Payload(claims.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthenticationResponse introspect(AuthenticationResponse authenticationResponse) {
        try {
            // Kiem tra xem token có bị blacklist không
            if (isTokenBlacklisted(authenticationResponse.getToken())) {
                throw new CustomExceptions.TokenException("This token has been revoked");
            }

            // 1. Lấy token từ AuthenticationResponse
            String token = authenticationResponse.getToken();

            // 1. Parse token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 2. Xác thực chữ ký
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            boolean verified = signedJWT.verify(verifier);

            // 3. Lấy thông tin claims
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expiryTime = claims.getExpirationTime();
            boolean notExpired = expiryTime != null && expiryTime.after(new Date());

            // 4. Chuyển đổi role từ String sang Enum Role
            Role role = null;
            String roleString = claims.getStringClaim("role");
            if (roleString != null) {
                try {
                    role = Role.valueOf(roleString); // Chuyển đổi từ String thành Enum
                } catch (IllegalArgumentException e) {
                    return buildInvalidAuthenticationResponse("Invalid role in token");
                }
            }

            // 5. Trả về kết quả phù hợp với AuthenticationResponse
            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(verified && notExpired)
                    .expiresIn(expiryTime != null ? expiryTime.getTime() / 1000 : null) // Chuyển sang giây
                    .tokenType("Bearer") // Giữ nguyên loại token
                    .userId(claims.getStringClaim("userId"))
                    .role(role)
                    .email(claims.getStringClaim("email"))
                    .build();

        } catch (Exception e) {
            return buildInvalidAuthenticationResponse("Token verification error");
        }
    }

    private AuthenticationResponse buildInvalidAuthenticationResponse(String errorMessage) {
        return AuthenticationResponse.builder()
                .token(null)
                .authenticated(false)
                .expiresIn(null)
                .tokenType("Bearer")
                .userId(null)
                .role(null)
                .email(null)
                .build();
    }

    @Override
    public void logout(String token) {
        // Nếu dùng Blacklist Token, ta có thể lưu token vào database hoặc cache như Redis
        tokenBlacklistService.addToBlacklist(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistService.isBlacklisted(token);
    }
}
