package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.dto.StaffHotelDto;
import com.example.QuanLyKhachSan.dto.StaffUserDto;
import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.service.StaffHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
public class StaffHotelController {

    @Autowired
    private StaffHotelService staffHotelService;

    @GetMapping("/{staffHotelId}")
    public ResponseEntity<ApiResponse<StaffHotelDto>> getStaffHotelById(@PathVariable String staffHotelId) {
        try {
            StaffHotelDto staffHotelDto = staffHotelService.getStaffById(staffHotelId);
            return ResponseEntity.ok(new ApiResponse<>("Staff hotel retrieved successfully", staffHotelDto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>("Error retrieving staff hotel: " + e.getMessage(), null));
        }
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllStaffHotels(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("All staff hotels", staffHotelService.getAllStaffHotels(page, size)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>("Error retrieving staff hotels: " + e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<StaffHotelDto>> addStaffHotel(@RequestBody StaffUserDto staffHotelDto) {
        try {
            StaffHotelDto createdStaffHotel = staffHotelService.addStaff(staffHotelDto);
            return ResponseEntity.ok(new ApiResponse<>("Staff hotel added successfully", createdStaffHotel));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>("Error adding staff hotel: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{staffHotelId}")
    public ResponseEntity<ApiResponse<StaffHotelDto>> updateStaffHotel(@PathVariable String staffHotelId, @RequestBody StaffUserDto staffHotelDto) {
        try {
            StaffHotelDto updatedStaffHotel = staffHotelService.updateStaff(staffHotelId,staffHotelDto);
            return ResponseEntity.ok(new ApiResponse<>("Staff hotel updated successfully", updatedStaffHotel));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>("Error updating staff hotel: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{staffHotelId}")
    public ResponseEntity<ApiResponse<String>> deleteStaffHotel(@PathVariable String staffHotelId) {
        try {
            staffHotelService.deleteStaff(staffHotelId);
            return ResponseEntity.ok(new ApiResponse<>("Staff hotel deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>("Error deleting staff hotel: " + e.getMessage(), null));
        }
    }
    @GetMapping("/hotel/{userid}")
    public ResponseEntity<ApiResponse<?>> getHotelIdByUserId(@PathVariable String userid) {
        try {
            return ResponseEntity.ok( ApiResponse.success("Hotel ID found successfully", staffHotelService.getHotelIdByUserId(userid)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse<>("Error retrieving staff hotel by hotel ID: ", e.getMessage()));
        }
    }
}
