package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.RoomTypeDto;
import com.example.QuanLyKhachSan.entity.Amenity;
import com.example.QuanLyKhachSan.entity.RoomType;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.repository.RoomTypeRepository;
import com.example.QuanLyKhachSan.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Override
    public RoomTypeDto getRoomTypeById(String roomTypeId) {
        if (roomTypeId == null || roomTypeId.isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Room type ID cannot be null or empty");
        }
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Room type not found with ID: " + roomTypeId));
        return RoomTypeDto.fromEntity(roomType);
    }

    @Override
    public RoomTypeDto getRoomTypeByName(String roomTypeName) {
        if (roomTypeName == null || roomTypeName.isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Room type name cannot be null or empty");
        }
        RoomType roomType = roomTypeRepository.findByRoomTypeName(roomTypeName)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Room type not found with name: " + roomTypeName));
        return RoomTypeDto.fromEntity(roomType);
    }

    @Override
    @Transactional
    public List<RoomTypeDto> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        if (roomTypes.isEmpty()) {
            throw new CustomExceptions.ResourceNotFoundException("No room types found");
        }
        return RoomTypeDto.fromEntityList(roomTypes);
    }

    @Override
    public RoomTypeDto adddRoomType(RoomTypeDto roomTypeDto) {
        if (roomTypeDto == null || roomTypeDto.getRoomTypeName() == null || roomTypeDto.getRoomTypeName().isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Room type name cannot be null or empty");
        }
        RoomType roomType = new RoomType();
        roomType.setRoomTypeName(roomTypeDto.getRoomTypeName());
        roomType.setAmenities(roomTypeDto.getAmenityIds().stream()
                .map(amenityId -> new Amenity(amenityId))
                .collect(Collectors.toList()));
        RoomType savedRoomType = roomTypeRepository.save(roomType);
        return RoomTypeDto.fromEntity(savedRoomType);
    }

    @Override
    public RoomTypeDto updateRoomType(String roomTypeId, RoomTypeDto roomTypeDto) {
        if (roomTypeId == null || roomTypeId.isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Room type ID cannot be null or empty");
        }
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Room type not found with ID: " + roomTypeId));
        if (roomTypeDto.getRoomTypeName() != null && !roomTypeDto.getRoomTypeName().isEmpty()) {
            roomType.setRoomTypeName(roomTypeDto.getRoomTypeName());
        }
        RoomType updatedRoomType = roomTypeRepository.save(roomType);
        return RoomTypeDto.fromEntity(updatedRoomType);
    }

    @Override
    public void deleteRoomType(String roomTypeId) {
        if (roomTypeId == null || roomTypeId.isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Room type ID cannot be null or empty");
        }
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Room type not found with ID: " + roomTypeId));
        roomTypeRepository.delete(roomType);
    }
}
