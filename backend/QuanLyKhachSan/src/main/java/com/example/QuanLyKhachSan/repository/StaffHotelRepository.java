package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.StaffHotel;
import com.example.QuanLyKhachSan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffHotelRepository extends JpaRepository<StaffHotel, String> {
    Optional<StaffHotel> findByUser(User user);

}
