package com.example.QuanLyKhachSan.entity;

import com.example.QuanLyKhachSan.enums.Role;
import com.example.QuanLyKhachSan.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private String userId;

    @Column(name="email", unique = true, nullable = false, length = 255)
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email phải có định dạng hợp lệ")
    private String email;

    @Column(name="password", nullable = true, length = 255)
    private String password;

    @Column(name = "role", length = 255)
    @Enumerated(EnumType.STRING)
    private Role role;         // nvarchar(255)

    @Column(name="created_at")
    private LocalDate createdAt;       // date

    @Column(name="google_id", unique = true, nullable = true, length = 255)
    private String googleId;      // nvarchar(255)

    @Column(name = "user_status", length = 255)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;   // nvarchar(255)
    public static String encodePassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
