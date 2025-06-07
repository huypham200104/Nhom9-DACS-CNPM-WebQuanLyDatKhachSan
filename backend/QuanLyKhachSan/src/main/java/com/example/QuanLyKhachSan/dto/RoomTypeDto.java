package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Amenity;
import com.example.QuanLyKhachSan.entity.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RoomTypeDto {
    private String roomTypeId; // Phải là String, không phải int hoặc long
    private String roomTypeName;
    private List<Integer> amenityIds; // Danh sách ID của amenities, kiểu int

    public static RoomTypeDto fromEntity(RoomType roomType) {
        RoomTypeDto dto = new RoomTypeDto();
        dto.setRoomTypeId(roomType.getRoomTypeId()); // String UUID
        dto.setRoomTypeName(roomType.getRoomTypeName());
        dto.setAmenityIds(roomType.getAmenities().stream()
                .map(Amenity::getAmenityId) // Lấy amenityId kiểu int
                .collect(Collectors.toList()));
        return dto;
    }

    public static List<RoomTypeDto> fromEntityList(List<RoomType> roomTypes) {
        return roomTypes.stream()
                .map(RoomTypeDto::fromEntity)
                .collect(Collectors.toList());
    }
}