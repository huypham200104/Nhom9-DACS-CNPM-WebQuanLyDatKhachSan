package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.UserDto;
import com.example.QuanLyKhachSan.entity.User;

import java.util.List;

public interface UserService {
    UserDto addUser(User user);
    UserDto getUserById(String userId);
    List<UserDto> getAllUsers(int page, int size); // Thêm phân trang
    UserDto updateUser(String userId, User user);
    void deleteUser(String userId);
    UserDto findByEmail(String email);
    UserDto resetPassword(String email);
}