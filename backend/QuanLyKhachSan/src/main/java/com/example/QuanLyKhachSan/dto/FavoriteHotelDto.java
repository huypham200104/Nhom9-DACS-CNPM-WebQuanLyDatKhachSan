package com.example.QuanLyKhachSan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteHotelDto {

    private String id;

    private String customerId;

    private String hotelId;

    private LocalDateTime createdAt;
}