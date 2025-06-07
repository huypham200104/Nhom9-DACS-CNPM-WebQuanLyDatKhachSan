package com.example.QuanLyKhachSan.response;

import com.example.QuanLyKhachSan.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private boolean authenticated;
    private Long expiresIn; // Thời gian hết hạn (giây)
    private String tokenType; // Loại token (ví dụ: "Bearer")
    private String userId; // ID người dùng dạng UUID
    private Role role; // Vai trò của người dùng (ví dụ: "ADMIN", "USER")
    private String email; // Email của người dùng
}
