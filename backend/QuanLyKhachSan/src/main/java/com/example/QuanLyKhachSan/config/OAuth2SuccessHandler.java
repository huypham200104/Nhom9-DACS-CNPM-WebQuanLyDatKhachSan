package com.example.QuanLyKhachSan.config;

import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.enums.Role;
import com.example.QuanLyKhachSan.enums.UserStatus;
import com.example.QuanLyKhachSan.repository.CustomerRepository;
import com.example.QuanLyKhachSan.repository.UserRepository;
import com.example.QuanLyKhachSan.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final AuthenticationService authenticationService;
    private final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            logger.debug("OAuth2 User Attributes: {}", oAuth2User.getAttributes());

            // Lấy thông tin từ OAuth2User
            String email = extractEmail(oAuth2User);
            String name = extractName(oAuth2User);

            logger.debug("Extracted email: {}", email);
            logger.debug("Extracted name: {}", name);

            if (email == null) {
                logger.error("Không thể lấy email từ tài khoản OAuth2");
                redirectWithError(response, "Không thể lấy email từ tài khoản OAuth2");
                return;
            }

            // Tìm hoặc tạo user
            User user = findOrCreateUser(email, name);

            // Tạo token
            String token = authenticationService.generateToken(user);

            // Tạo URL redirect với thông tin user
            String redirectUrl = buildRedirectUrl(user, token);

            logger.debug("Redirecting to: {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            logger.error("OAuth2 Success Handler Error: {}", e.getMessage(), e);
            redirectWithError(response, "Đăng nhập OAuth2 thất bại: " + e.getMessage());
        }
    }

    private String extractEmail(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Google
        String email = (String) attributes.get("email");
        if (email != null) {
            return email;
        }

        // GitHub - thử nhiều cách
        email = (String) attributes.get("email");
        if (email != null) {
            return email;
        }

        // GitHub backup - sử dụng login + domain
        String login = (String) attributes.get("login");
        if (login != null) {
            return login + "@github.local";
        }

        return null;
    }

    private String extractName(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Thử lấy name
        String name = (String) attributes.get("name");
        if (name != null && !name.trim().isEmpty()) {
            return name;
        }

        // GitHub - thử login
        String login = (String) attributes.get("login");
        if (login != null) {
            return login;
        }

        // Google - thử given_name + family_name
        String givenName = (String) attributes.get("given_name");
        String familyName = (String) attributes.get("family_name");
        if (givenName != null || familyName != null) {
            return (givenName != null ? givenName : "") + " " + (familyName != null ? familyName : "");
        }

        return "OAuth2 User";
    }

    @Transactional
    public User findOrCreateUser(String email, String name) {
        logger.debug("Tìm user với email: {}", email);
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            logger.debug("Tìm thấy user với email: {}", email);
            return existingUser.get();
        }

        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                logger.debug("Tạo user mới với email: {}, lần thử: {}", email, attempt);
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setRole(Role.CUSTOMER);
                newUser.setPassword("oauth2_user");
                newUser.setCreatedAt(LocalDate.now());
                newUser.setUserStatus(UserStatus.ACTIVE);

                User savedUser = userRepository.saveAndFlush(newUser); // Sử dụng saveAndFlush để đảm bảo commit

                Customer customer = new Customer();
                customer.setCustomerName(name);
                customer.setCustomerName(name);
                customer.setUser(savedUser);
                customerRepository.saveAndFlush(customer);

                return savedUser;
            } catch (ObjectOptimisticLockingFailureException e) {
                logger.warn("Xung đột lạc quan khi lưu user với email: {}, lần thử: {}", email, attempt, e);
                if (attempt == maxRetries) {
                    logger.error("Không thể lưu user sau {} lần thử", maxRetries, e);
                    throw new RuntimeException("Không thể lưu user sau nhiều lần thử", e);
                }
            } catch (Exception e) {
                logger.error("Lỗi khi lưu user hoặc customer với email: {}", email, e);
                throw new RuntimeException("Không thể lưu user hoặc customer: " + e.getMessage(), e);
            }
        }
        throw new RuntimeException("Không thể lưu user sau nhiều lần thử");
    }

    private String buildRedirectUrl(User user, String token) {
        try {
            String baseUrl = "http://localhost:3000/oauth2/callback";

            String tokenParam = URLEncoder.encode(token, StandardCharsets.UTF_8);
            String roleParam = URLEncoder.encode(user.getRole().toString(), StandardCharsets.UTF_8);
            String userIdParam = URLEncoder.encode(user.getUserId(), StandardCharsets.UTF_8);
            String emailParam = URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);

            return String.format("%s?token=%s&role=%s&userId=%s&email=%s",
                    baseUrl, tokenParam, roleParam, userIdParam, emailParam);

        } catch (Exception e) {
            logger.error("Lỗi khi tạo redirect URL: {}", e.getMessage(), e);
            return "http://localhost:3000/login?error=redirect_failed";
        }
    }

    private void redirectWithError(HttpServletResponse response, String errorMessage) throws IOException {
        logger.error("Chuyển hướng với lỗi: {}", errorMessage);
        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        response.sendRedirect("http://localhost:3000/login?error=" + encodedError);
    }
}