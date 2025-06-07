package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.dto.DiscountDto;
import com.example.QuanLyKhachSan.entity.Discount;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/discounts")
@RestController
public class DiscountController {
    @Autowired
    private DiscountService discountService;
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<?>> getDiscountById(String discountId) {
        try{
            DiscountDto discount = discountService.getDiscountById(discountId);
            return ResponseEntity.ok(new ApiResponse<>("Get discount successfully", discount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error fetching hotel", e.getMessage()));
        }
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllDiscounts() {
        try {
            List<DiscountDto> discounts = discountService.getAllDiscounts();
            return ResponseEntity.ok(new ApiResponse<>("Get all discounts successfully", discounts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error fetching all discounts", e.getMessage()));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addDiscount(@RequestBody Discount discount)
    {
        try {
            DiscountDto createdDiscount = discountService.addDiscount(discount, discount.getHotel().getHotelId());
            return ResponseEntity.ok(new ApiResponse<>("Create discount successfully", createdDiscount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error creating discount", e.getMessage()));
        }
    }
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateDiscount(@RequestBody Discount discount) {
        try {
            DiscountDto updatedDiscount = discountService.updateDiscount(discount, discount.getHotel().getHotelId());
            return ResponseEntity.ok(new ApiResponse<>("Update discount successfully", updatedDiscount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error updating discount", e.getMessage()));
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteDiscount(@PathVariable String id) {
        try {
            discountService.deleteDiscount(id);
            return ResponseEntity.ok(new ApiResponse<>("Delete discount successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error deleting discount", e.getMessage()));
        }
    }
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<?>> getDiscountByCode(@PathVariable String discountCode) {
        try {
            DiscountDto discount = discountService.getDiscountByCode(discountCode);
            return ResponseEntity.ok(new ApiResponse<>("Get discount by code successfully", discount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error fetching discount by code", e.getMessage()));
        }
    }
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<?>> getDiscountsByHotelId(@PathVariable String hotelId) {
        try {
            List<DiscountDto> discounts = discountService.getDiscountsByHotelId(hotelId);
            return ResponseEntity.ok(new ApiResponse<>("Get discounts by hotel ID successfully", discounts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error fetching discounts by hotel ID", e.getMessage()));
        }
    }
    @PostMapping("/validate")
    public ResponseEntity<DiscountDto> validateDiscount(
            @RequestParam String discountCode,
            @RequestParam Long bookingAmount,
            @RequestParam String hotelId) {
        try {
            Discount discount = discountService.validateDiscount(discountCode, bookingAmount, hotelId);
            DiscountDto discountDto = new DiscountDto().toDto(discount, hotelId);
            return ResponseEntity.ok(discountDto);
        } catch (CustomExceptions.InvalidInputException | CustomExceptions.ResourceNotFoundException e) {
            throw e;
        }
    }

}
