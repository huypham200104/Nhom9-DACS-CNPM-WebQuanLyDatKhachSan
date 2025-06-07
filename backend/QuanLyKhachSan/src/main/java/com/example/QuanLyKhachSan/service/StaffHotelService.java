package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.StaffHotelDto;
import com.example.QuanLyKhachSan.dto.StaffHotelWithHotelNameDto;
import com.example.QuanLyKhachSan.dto.StaffUserDto;
import com.example.QuanLyKhachSan.entity.StaffHotel;

import java.util.List;

public interface StaffHotelService {
    StaffHotelDto addStaff(StaffUserDto staffHotel);
    StaffHotelDto updateStaff(String staffHotelId, StaffUserDto staffHotel);
    StaffHotelDto getStaffById(String staffId);
    List<StaffHotelWithHotelNameDto> getAllStaffHotels(int page, int size);
    void deleteStaff(String staffId);
    String getHotelIdByUserId(String userId);
}
