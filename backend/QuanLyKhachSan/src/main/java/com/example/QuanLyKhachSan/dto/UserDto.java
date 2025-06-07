package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.enums.Role;
import com.example.QuanLyKhachSan.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String email;
    private String password;
    private Role role;
    private LocalDate createdAt;
    private String googleId;
    private UserStatus userStatus;
    public static UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setGoogleId(user.getGoogleId());
        userDto.setUserStatus(user.getUserStatus());
        return userDto;
    }
    public static User toUserEntity(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setGoogleId(userDto.getGoogleId());
        user.setUserStatus(userDto.getUserStatus());
        return user;
    }
}