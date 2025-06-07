package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.ImageHotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageHotelRepository extends JpaRepository<ImageHotel, String> {
    List<ImageHotel> findByHotelIn(List<Hotel> hotels);

    List<ImageHotel> findByHotel(Hotel hotel);
}
