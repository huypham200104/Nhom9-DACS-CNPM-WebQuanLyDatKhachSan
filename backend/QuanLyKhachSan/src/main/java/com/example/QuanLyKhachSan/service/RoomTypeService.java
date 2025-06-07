package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.RoomTypeDto;

import java.util.List;

public interface RoomTypeService {
    RoomTypeDto getRoomTypeById(String roomTypeId);
    RoomTypeDto getRoomTypeByName(String roomTypeName);
    List<RoomTypeDto> getAllRoomTypes();
    RoomTypeDto adddRoomType(RoomTypeDto roomTypeDto);
    RoomTypeDto updateRoomType(String roomTypeId, RoomTypeDto roomTypeDto);
    void deleteRoomType(String roomTypeId);

}
