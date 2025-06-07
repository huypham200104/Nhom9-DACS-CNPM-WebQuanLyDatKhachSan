package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.service.FavoriteHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite-hotel")
public class FavoriteHotelController {
    @Autowired
    private FavoriteHotelService favoriteHotelService;

    @GetMapping("/get-all/{userId}")
    public ResponseEntity<ApiResponse<?>> getAllFavoriteHotelByUserId(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Get all favorite hotel success", favoriteHotelService.getFavoriteHotelsByUserId(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("Get all favorite hotel failed", e.getMessage()));
        }
    }

    @PostMapping ("/add/{userId}/{hotelId}")
    public ResponseEntity<ApiResponse<Void>> addFavoriteHotel(@PathVariable String userId, @PathVariable String hotelId) {
        try {
            favoriteHotelService.addFavoriteHotel(userId, hotelId);
            return ResponseEntity.ok(new ApiResponse<>("Add favorite hotel success"));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("Add favorite hotel failed", e.getMessage()));
        }
    }

    @GetMapping("/remove/{userId}/{hotelId}")
    public ResponseEntity<ApiResponse<Void>> removeFavoriteHotel(@PathVariable String userId, @PathVariable String hotelId) {
        try {
            favoriteHotelService.removeFavoriteHotel(userId, hotelId);
            return ResponseEntity.ok(new ApiResponse<>("Remove favorite hotel success"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("Remove favorite hotel failed", e.getMessage()));
        }
    }
}