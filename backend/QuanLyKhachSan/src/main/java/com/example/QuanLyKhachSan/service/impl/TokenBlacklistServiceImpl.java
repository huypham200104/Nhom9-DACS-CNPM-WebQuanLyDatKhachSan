package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.service.TokenBlacklistService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final Set<String> blacklist = new HashSet<>();

    @Override
    public void addToBlacklist(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        blacklist.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        if (token == null || token.isEmpty()) {
            return false; // Hoặc throw exception tùy yêu cầu
        }
        return blacklist.contains(token);
    }
}
