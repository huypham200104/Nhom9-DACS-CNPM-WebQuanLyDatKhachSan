package com.example.QuanLyKhachSan.controller;


import com.example.QuanLyKhachSan.dto.RoomDto;
import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/rooms")
@RestController
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;
    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllRoom(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<RoomDto> rooms = roomService.getAllRooms(page, size);
            return ResponseEntity.ok(new ApiResponse<>("Get all rooms successfully", rooms));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving rooms: " + e.getMessage()));
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<?>> getRoomById(@PathVariable String roomId) {
        try {
            RoomDto room = roomService.getRoomById(roomId);
            return ResponseEntity.ok(new ApiResponse<>("Get room successfully", room));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving room: " + e.getMessage()));
        }
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> addRoom(
            @RequestPart(value = "roomDto", required = true) String roomDtoJson, // Nhận chuỗi JSON từ text/plain
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            logger.info("Received roomDtoJson: {}, files: {}", roomDtoJson, files != null ? files.size() : 0);

            // Parse chuỗi JSON thành RoomDTO
            RoomDto roomDto;
            try {
                roomDto = objectMapper.readValue(roomDtoJson, RoomDto.class);
            } catch (IOException e) {
                logger.error("Failed to parse roomDto JSON", e);
                return ResponseEntity.status(400)
                        .body(new ApiResponse<>("Failed to parse roomDto JSON: " + e.getMessage(), e.getClass().getName()));
            }

            logger.info("Parsed RoomDTO: {}", roomDto);
            RoomDto roomWithImageDto = roomService.addRoom(roomDto, files);
            return ResponseEntity.ok(new ApiResponse<>("Room added successfully", roomWithImageDto));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage(), e);
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>("Invalid argument: " + e.getMessage(), e.getClass().getName()));
        } catch (Exception e) {
            logger.error("Error adding room: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error adding room: " + e.getMessage(), e.getClass().getName()));
        }
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<ApiResponse<?>> updateRoom(
            @PathVariable String roomId,
            @ModelAttribute RoomDto roomDTO,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            RoomDto updatedRoom = roomService.updateRoom(roomId, roomDTO, files);
            return ResponseEntity.ok(new ApiResponse<>("Room updated successfully", updatedRoom));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error updating room: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<?>> deleteRoom(@PathVariable String roomId) {
        try {
            roomService.deleteRoom(roomId);
            return ResponseEntity.ok(new ApiResponse<>("Room deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error deleting room: " + e.getMessage()));
        }
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<?>> getRoomByHotelId(@PathVariable String hotelId) {
        try {
            List<RoomDto> rooms = roomService.getRoomByHotelId(hotelId);
            return ResponseEntity.ok(new ApiResponse<>("Get rooms by hotel ID successfully", rooms));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving rooms by hotel ID: " + e.getMessage()));
        }
    }
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<?>> getAvailableRooms(
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam Integer adults,
            @RequestParam(required = false) Integer children,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<RoomDto> availableRooms = roomService.getAvailableRooms(city, checkInDate, checkOutDate, adults, children, page, size);
            return ResponseEntity.ok(new ApiResponse<>("Get available rooms successfully", availableRooms));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving available rooms: " + e.getMessage()));
        }
    }
    @GetMapping("/{roomId}/available-dates")
    public ResponseEntity<List<String>> getAvailableDatesForRoom(
            @PathVariable String roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            // Call the service method to get available dates
            List<LocalDate> availableDates = roomService.getAvailableDatesForRoom(roomId, startDate, endDate);

            // Convert LocalDate list to String list in yyyy-MM-dd format
            List<String> availableDatesStr = availableDates.stream()
                    .map(LocalDate::toString)
                    .collect(Collectors.toList());

            // Return the response with HTTP 200 OK
            return ResponseEntity.ok(availableDatesStr);
        } catch (IllegalArgumentException e) {
            // Handle cases like invalid room ID or date range
            return ResponseEntity.badRequest().body(List.of("Error: " + e.getMessage()));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(500).body(List.of("Internal server error: " + e.getMessage()));
        }
    }
    @GetMapping("/hotel/{hotelId}/rooms")
    public ResponseEntity<ApiResponse<?>> getRoomsByHotelId(
            @PathVariable String hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<RoomDto> rooms = roomService.getRoomsByHotelId(hotelId, page, size);
            return ResponseEntity.ok(new ApiResponse<>("Get rooms by hotel ID successfully", rooms));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving rooms by hotel ID: " + e.getMessage()));
        }
    }
    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<?>> getRoomByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<RoomDto> rooms = roomService.getRoomByCity(city, page, size);
            return ResponseEntity.ok(new ApiResponse<>("Get rooms by city successfully", rooms));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>("Error retrieving rooms by city: " + e.getMessage()));
        }
    }
}