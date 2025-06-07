package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
    Optional<RoomType> findByRoomTypeName(String roomTypeName);
}
