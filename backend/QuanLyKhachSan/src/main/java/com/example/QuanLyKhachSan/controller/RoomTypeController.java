package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.dto.RoomTypeDto;
import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room-types")
public class RoomTypeController {
    @Autowired
    private RoomTypeService roomTypeService;
    @GetMapping("{roomTypeId}")
    public ResponseEntity<ApiResponse<?>> getRoomTypeById(@PathVariable String roomTypeId) {
        try {
            RoomTypeDto roomType = roomTypeService.getRoomTypeById(roomTypeId);
            return ResponseEntity.ok(new ApiResponse<>("Get room type successfully", roomType));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving room type: " + e.getMessage()));
        }
    }
    @GetMapping("/name/{roomTypeName}")
    public ResponseEntity<ApiResponse<?>> getRoomTypeByName(@PathVariable String roomTypeName) {
        try {
            RoomTypeDto roomType = roomTypeService.getRoomTypeByName(roomTypeName);
            return ResponseEntity.ok(new ApiResponse<>("Get room type successfully", roomType));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving room type: " + e.getMessage()));
        }
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllRoomTypes() {
        try {
            List<RoomTypeDto> roomTypes = roomTypeService.getAllRoomTypes();
            return ResponseEntity.ok(new ApiResponse<>("Get all room types successfully", roomTypes));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving room types: " + e.getMessage()));
        }
    }
    @PostMapping
    public ResponseEntity<ApiResponse<?>> addRoomType(@RequestBody RoomTypeDto roomTypeDto) {
        try {
            RoomTypeDto newRoomType = roomTypeService.adddRoomType(roomTypeDto);
            return ResponseEntity.ok(new ApiResponse<>("Add room type successfully", newRoomType));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error adding room type: " + e.getMessage()));
        }
    }
    @PutMapping("/{roomTypeId}")
    public ResponseEntity<ApiResponse<?>> updateRoomType(@PathVariable String roomTypeId, @RequestBody RoomTypeDto roomTypeDto) {
        try {
            RoomTypeDto updatedRoomType = roomTypeService.updateRoomType(roomTypeId, roomTypeDto);
            return ResponseEntity.ok(new ApiResponse<>("Update room type successfully", updatedRoomType));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error updating room type: " + e.getMessage()));
        }
    }
    @DeleteMapping("/{roomTypeId}")
    public ResponseEntity<ApiResponse<?>> deleteRoomType(@PathVariable String roomTypeId) {
        try {
            roomTypeService.deleteRoomType(roomTypeId);
            return ResponseEntity.ok(new ApiResponse<>("Delete room type successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error deleting room type: " + e.getMessage()));
        }
    }
}
