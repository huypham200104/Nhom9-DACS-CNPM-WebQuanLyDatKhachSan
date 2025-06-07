package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.dto.BookingDto;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Slf4j
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Lấy tất cả booking với phân trang
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookingDto>>> getAllBookings(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            Page<BookingDto> bookings = bookingService.getAllBookings(page, size);
            return ResponseEntity.ok(new ApiResponse<>("Bookings retrieved successfully", bookings));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for getAllBookings: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error retrieving bookings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error retrieving bookings: " + e.getMessage(), null));
        }
    }

    /**
     * Tạo booking mới
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingDto>> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        try {
            BookingDto createdBooking = bookingService.createBooking(bookingDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Booking created successfully", createdBooking));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for createBooking: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error creating booking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error creating booking: " + e.getMessage(), null));
        }
    }

    /**
     * Lấy booking theo ID
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDto>> getBookingById(@PathVariable String bookingId) {
        try {
            Optional<BookingDto> booking = bookingService.getBookingById(bookingId);
            if (booking.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>("Booking retrieved successfully", booking.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Booking not found", null));
            }
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for getBookingById: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error retrieving booking with ID: {}", bookingId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error retrieving booking: " + e.getMessage(), null));
        }
    }

    /**
     * Cập nhật booking
     */
    @PutMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDto>> updateBooking(
            @PathVariable String bookingId,
            @Valid @RequestBody BookingDto bookingDto) {
        try {
            BookingDto updatedBooking = bookingService.updateBooking(bookingId, bookingDto);
            return ResponseEntity.ok(new ApiResponse<>("Booking updated successfully", updatedBooking));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for updateBooking: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (CustomExceptions.ResourceNotFoundException e) {
            log.error("Booking not found for update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Booking not found: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error updating booking with ID: {}", bookingId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error updating booking: " + e.getMessage(), null));
        }
    }

    /**
     * Hủy booking
     */

    /**
     * Lấy booking theo customer ID
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getBookingsByCustomerId(@PathVariable String customerId) {
        try {
            List<BookingDto> bookings = bookingService.getBookingsByCustomerId(customerId);
            return ResponseEntity.ok(new ApiResponse<>("Customer bookings retrieved successfully", bookings));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for getBookingsByCustomerId: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error retrieving bookings for customer ID: {}", customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error retrieving customer bookings: " + e.getMessage(), null));
        }
    }

    /**
     * Lấy booking theo room ID
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getBookingsByRoomId(@PathVariable String roomId) {
        try {
            List<BookingDto> bookings = bookingService.getBookingsByRoomId(roomId);
            return ResponseEntity.ok(new ApiResponse<>("Room bookings retrieved successfully", bookings));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for getBookingsByRoomId: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error retrieving bookings for room ID: {}", roomId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error retrieving room bookings: " + e.getMessage(), null));
        }
    }


    /**
     * Tính tổng giá
     */
    @GetMapping("/calculate-price")
    public ResponseEntity<ApiResponse<Long>> calculateTotalPrice(
            @RequestParam String roomId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOutDate,
            @RequestParam(required = false) String discountId) {
        try {
            long totalPrice = bookingService.calculateTotalPrice(roomId, checkInDate, checkOutDate, discountId);
            return ResponseEntity.ok(new ApiResponse<>("Total price calculated successfully", totalPrice));
        } catch (Exception e) {
            log.error("Error calculating price for room ID: {}", roomId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error calculating price: " + e.getMessage(), null));
        }
    }

    @GetMapping("/status/{bookingStatus}")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getBookingsByStatus(@PathVariable String bookingStatus) {
        try {
            List<BookingDto> bookings = bookingService.getBookingsByStatus(bookingStatus);
            return ResponseEntity.ok(new ApiResponse<>("Bookings by status retrieved successfully", bookings));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for getBookingsByStatus: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error retrieving bookings by status: {}", bookingStatus, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error retrieving bookings by status: " + e.getMessage(), null));
        }
    }
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<Page<BookingDto>>> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            Page<BookingDto> bookings = bookingService.getBookingsByDateRange(startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>("Bookings by date range retrieved successfully", bookings));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for getBookingsByDateRange: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error retrieving bookings by date range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error retrieving bookings by date range: " + e.getMessage(), null));
        }
    }
    // Kiem tra booking hop le khong
    @GetMapping("/room/{roomId}/availability")
    public ResponseEntity<ApiResponse<Boolean>> checkRoomAvailability(
            @PathVariable String roomId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOutDate) {
        try {
            boolean isValid = bookingService.isRoomAvailable(roomId, checkInDate, checkOutDate);
            return ResponseEntity.ok(new ApiResponse<>("Room availability checked successfully", isValid));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for checkRoomAvailability: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error checking room availability for room ID: {}", roomId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error checking room availability: " + e.getMessage(), null));
        }
    }
 @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<Page<BookingDto>>> getBookingsByHotelId(
            @PathVariable String hotelId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            Page<BookingDto> bookings = bookingService.getBookingsByHotelId(hotelId, page, size);
            return ResponseEntity.ok(new ApiResponse<>("Bookings by hotel ID retrieved successfully", bookings));
        } catch (CustomExceptions.InvalidInputException e) {
            log.error("Invalid input for getBookingsByHotelId: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid input: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error retrieving bookings by hotel ID: {}", hotelId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error retrieving bookings by hotel ID: " + e.getMessage(), null));
        }
    }

}