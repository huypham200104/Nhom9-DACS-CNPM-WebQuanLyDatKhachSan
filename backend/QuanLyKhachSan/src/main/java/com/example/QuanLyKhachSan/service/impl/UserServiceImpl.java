package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.UserDto;
import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.enums.Role;
import com.example.QuanLyKhachSan.enums.UserStatus;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.repository.UserRepository;
import com.example.QuanLyKhachSan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;       // Interface Page
import org.springframework.data.domain.PageRequest;  // Lớp PageRequest
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;


    @Override
    public UserDto addUser(User user) {

        if(userRepository.existsByEmail(user.getEmail())){
            throw new CustomExceptions.ResourceAlreadyExistsException("Email already exists");
        }
        if(userRepository.existsByGoogleId(user.getGoogleId()) && user.getGoogleId() != null){
            throw new CustomExceptions.ResourceAlreadyExistsException("Google ID already exists");
        }
        user.setUserStatus(UserStatus.ACTIVE);
        user.setRole(user.getRole());
        user.setCreatedAt(LocalDate.now());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return UserDto.toUserDto(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("User not found"));
        return UserDto.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers(int page, int size) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));
        if (userPage.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No users found");
        }
        return userPage.map(UserDto::toUserDto).getContent();
    }

    @Override
    public UserDto updateUser(String userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("User not found"));
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new CustomExceptions.ResourceAlreadyExistsException("Email already exists");
            }
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            existingUser.setPassword(encodedPassword);
        }
        userRepository.save(existingUser);
        return UserDto.toUserDto(existingUser);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("User not found with email: " + email));
        return UserDto.toUserDto(user);
    }

    @Override
    public UserDto resetPassword(String email) {
        try {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("User not found with email: " + email));
        if(user.getPassword().equals("oauth2_user")) {
            throw new CustomExceptions.ResourceNotFoundException("User is an OAuth2 user, cannot reset password");
        }

        // Tạo mật khẩu mới (có thể là ngẫu nhiên hoặc theo quy tắc nào đó)
        String newPassword = generateRandomString(10); // Ví dụ tạo mật khẩu ngẫu nhiên 10 ký tự
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(newPassword));
        // Gửi email với mật khẩu mới
        emailService.sendSimpleEmail(email, newPassword);

        userRepository.save(user);
            return UserDto.toUserDto(user);
        }
        catch (Exception e) {
            throw new CustomExceptions.ResourceNotFoundException("Error resetting password: " + e.getMessage());
        }

    }
    // Tao chuoi ki tu ngau nhien
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

}
