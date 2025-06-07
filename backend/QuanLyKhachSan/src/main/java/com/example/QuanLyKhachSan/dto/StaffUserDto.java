package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Hotel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffUserDto {
    private String userId;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    private String role;
    private String googleId;
    @NotBlank(message = "Vị trí không được để trống")
    @Pattern(regexp = "^(MANAGER|RECEPTIONIST)$", message = "Vị trí phải là MANAGER hoặc RECEPTIONIST")
    private String position;
    private Hotel hotel;
    private LocalDate startDate;
    private LocalDate endDate;


}
