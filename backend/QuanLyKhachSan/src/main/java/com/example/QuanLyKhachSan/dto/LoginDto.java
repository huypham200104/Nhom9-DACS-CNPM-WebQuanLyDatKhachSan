package com.example.QuanLyKhachSan.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotNull(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được dài quá 100 ký tự")
    private String email;

    @NotNull(message = "Mật khẩu không được để trống")
    private String password;
}
