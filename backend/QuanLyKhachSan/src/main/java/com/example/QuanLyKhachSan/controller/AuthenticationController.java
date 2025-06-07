package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.dto.LoginDto;
import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.response.AuthenticationResponse;
import com.example.QuanLyKhachSan.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @Valid @RequestBody LoginDto loginDto,
            HttpServletRequest request,
            HttpServletResponse response) {
        logger.debug("Login request received for email: {}", loginDto.getEmail());

        if (loginDto.getEmail() == null || loginDto.getEmail().isBlank()
                || loginDto.getPassword() == null || loginDto.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Bad Request", "Email and password are required")
            );
        }

        try {
            // Xóa session và cookie OAuth2 trước khi đăng nhập thông thường
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            removeAccessTokenCookie(response);

            AuthenticationResponse authenticationResponse = authenticationService.authenticate(loginDto);
            String accessToken = authenticationResponse.getToken();

            if (authenticationService.isTokenBlacklisted(accessToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.error("Unauthorized", "This token has been revoked")
                );
            }

            createAccessTokenCookie(response, accessToken, authenticationResponse.getExpiresIn());

            return ResponseEntity.ok(ApiResponse.success("Login successful", authenticationResponse));

        } catch (RuntimeException e) {
            logger.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("Unauthorized", e.getMessage())
            );
        } catch (Exception e) {
            logger.error("Unexpected error during login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("Internal Server Error", "An unexpected error occurred: " + e.getMessage())
            );
        }
    }

    private void createAccessTokenCookie(HttpServletResponse response, String accessToken, Long expiresIn) {
        int maxAge = (expiresIn != null) ? expiresIn.intValue() : 3600;

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setMaxAge(maxAge);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // Đặt thành true nếu dùng HTTPS
        response.addCookie(accessTokenCookie);
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> introspect(@RequestBody AuthenticationResponse authResponse) {
        try {
            AuthenticationResponse response = authenticationService.introspect(authResponse);
            if (response == null || !response.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.error("Unauthorized", "Token is invalid or expired")
                );
            }
            return ResponseEntity.ok(ApiResponse.success("Token is valid", response));

        } catch (Exception e) {
            logger.error("Introspect error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.error("Internal Server Error", "An unexpected error occurred: " + e.getMessage())
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        logger.debug("Logout request received");

        try {
            // Lấy token từ cookie
            String accessToken = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[]{})
                    .filter(c -> "accessToken".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            // Thêm token vào blacklist nếu có
            if (accessToken != null && !accessToken.isBlank()) {
                authenticationService.logout(accessToken);
                logger.debug("Token blacklisted: {}", accessToken);
            }

            // Xóa cookie và session
            removeAccessTokenCookie(response);
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
                logger.debug("Session invalidated");
            }

            return ResponseEntity.ok()
                    .header("Cache-Control", "no-store, no-cache, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
                    .body(ApiResponse.success("Logout successful", null));

        } catch (Exception e) {
            logger.error("Logout error: {}", e.getMessage(), e);
            removeAccessTokenCookie(response);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Logout completed with errors",
                            "Session was cleared but server encountered an error: " + e.getMessage()));
        }
    }

    private void removeAccessTokenCookie(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // Đặt thành true nếu dùng HTTPS
        response.addCookie(accessTokenCookie);
        logger.debug("Access token cookie removed");
    }
}