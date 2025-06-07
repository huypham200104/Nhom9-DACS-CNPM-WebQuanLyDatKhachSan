package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.RoomDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    RoomDto getRoomById(String roomId);
    Page<RoomDto> getAllRooms(int page, int size);
    RoomDto addRoom(RoomDto roomDTO, List<MultipartFile> files);
    RoomDto updateRoom(String roomId, RoomDto roomDTO, List<MultipartFile> files);
    void deleteRoom(String roomId);
    List<RoomDto> getRoomByHotelId(String hotelId);
    Page<RoomDto> getAvailableRooms(String city, LocalDate checkInDate, LocalDate checkOutDate, Integer adults, Integer Chilren,int page, int size);
    List<LocalDate> getAvailableDatesForRoom(String roomId, LocalDate startDate, LocalDate endDate);
    Page<RoomDto> getRoomsByHotelId(String hotelId, int page, int size);
    Page<RoomDto> getRoomByCity(String city, int page, int size);
}