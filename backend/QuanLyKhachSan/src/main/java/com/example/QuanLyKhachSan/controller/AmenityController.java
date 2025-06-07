package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.entity.Amenity;
import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.service.AmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/amenities")
public class AmenityController {
    @Autowired
    private AmenityService amenityService;
    @GetMapping("/{amenityId}")
    public ResponseEntity<ApiResponse<?>> getAmenityById(@PathVariable   int amenityId) {
        try{
            return ResponseEntity.ok(new ApiResponse<>("Amenity found", amenityService.getAmenityById(amenityId)));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error fetching amenity", e.getMessage()));
        }
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllAmenities() {
        try {
            return ResponseEntity.ok(new ApiResponse<>("All amenities", amenityService.getAllAmenities()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error fetching amenities", e.getMessage()));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> addAmenity(@RequestBody Amenity amenity) {
        try {
            return ResponseEntity.ok(amenityService.addAmenity(amenity));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding amenity: " + e.getMessage());
        }
    }
    @PutMapping("/update/{amenityId}")
    public ResponseEntity<?> updateAmenity(@PathVariable int amenityId, @RequestBody Amenity amenity) {
        try {
            return ResponseEntity.ok(amenityService.updateAmenity(amenityId, amenity));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating amenity: " + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{amenityId}")
    public ResponseEntity<?> deleteAmenity(@PathVariable int amenityId) {
        try {
            amenityService.deleteAmenity(amenityId);
            return ResponseEntity.ok("Amenity deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting amenity: " + e.getMessage());
        }
    }
}
