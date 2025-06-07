package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.ImageRoom;
import com.example.QuanLyKhachSan.entity.Room;
import com.example.QuanLyKhachSan.enums.BedType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private String roomId;
    private LocalDate createdAt;
    private String description;
    private Integer maxGuests;
    private Long price;
    private Long roomSize;
    private String roomStatus;
    private LocalDate updatedAt;
    private String hotelId;
    private String roomTypeId;
    private BedType bedType;
    private List<ImageRoomDto> imageRooms;

    public static RoomDto fromEntity(Room room, List<ImageRoom> imageRooms) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(room.getRoomId());
        roomDto.setCreatedAt(room.getCreatedAt());
        roomDto.setDescription(room.getDescription());
        roomDto.setMaxGuests(room.getMaxGuests());
        roomDto.setPrice(room.getPrice());
        roomDto.setRoomSize(room.getRoomSize());
        roomDto.setRoomStatus(room.getRoomStatus());
        roomDto.setUpdatedAt(room.getUpdatedAt());
        roomDto.setHotelId(room.getHotel().getHotelId());
        roomDto.setRoomTypeId(room.getRoomType().getRoomTypeId());
        roomDto.setBedType(room.getBedType());

        if (imageRooms != null) {
            roomDto.setImageRooms(imageRooms.stream()
                    .map(imageRoom -> {
                        try {
                            return ImageRoomDto.fromEntity(imageRoom);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to read image file: " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList()));
        }

        return roomDto;
    }
}