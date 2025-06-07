package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {
    boolean existsByAmenityName(String amenityName);
}
