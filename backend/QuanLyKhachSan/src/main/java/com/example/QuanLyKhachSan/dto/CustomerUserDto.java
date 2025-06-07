package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.enums.Role;
import com.example.QuanLyKhachSan.enums.UserStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserDto {
    private String customerId;
    private String userId;

    @NotBlank(message = "Tên không được để trống")
    private String customerName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải có đúng 10 chữ số")
    private String phone;

    private String role;

    @NotNull(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được dài quá 100 ký tự")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "Mật khẩu phải dài 8-16 ký tự, chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt"
    )
    private String password;

    private LocalDate createdAt;

    private String googleId;

    private String userStatus;
    public static UserDto toUserDto(CustomerUserDto userCustomerDto) {
        UserDto userDto = new UserDto();
        userDto.setUserId(userCustomerDto.getUserId());
        userDto.setEmail(userCustomerDto.getEmail());
        userDto.setPassword(userCustomerDto.getPassword());
        userDto.setRole(Role.valueOf(userCustomerDto.getRole()));
        userDto.setCreatedAt(userCustomerDto.getCreatedAt());
        userDto.setGoogleId(userCustomerDto.getGoogleId());
        userDto.setUserStatus(UserStatus.valueOf(userCustomerDto.getUserStatus()));
        return userDto;
    }

    // Chuyển từ CustomerUserDto sang Customer
    public static CustomerDto toCustomerDto(CustomerUserDto customerUserDto) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerName(customerUserDto.getCustomerName());
        customerDto.setPhone(customerUserDto.getPhone());
        customerDto.setUserId(customerUserDto.getUserId());
        return customerDto;
    }
}
