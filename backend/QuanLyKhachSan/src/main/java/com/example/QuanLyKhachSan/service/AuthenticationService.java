package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.LoginDto;
import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.response.AuthenticationResponse;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(LoginDto loginDto) throws ParseException;
    String generateToken(User user);
    AuthenticationResponse introspect(AuthenticationResponse loginDto);
    void logout(String token);
    boolean isTokenBlacklisted(String token);

}
