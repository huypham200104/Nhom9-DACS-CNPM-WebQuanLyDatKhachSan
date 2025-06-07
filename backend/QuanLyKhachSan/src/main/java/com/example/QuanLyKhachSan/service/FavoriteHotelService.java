package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.entity.Hotel;

import java.util.List;

public interface FavoriteHotelService {
    void addFavoriteHotel(String userId, String hotelId);

    void removeFavoriteHotel(String userId, String hotelId);

    List<Hotel> getFavoriteHotelsByUserId(String userId);
}
