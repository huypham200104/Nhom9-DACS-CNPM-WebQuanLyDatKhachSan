package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {
    private String feedbackId;
    private String hotelId; // Chỉ trả về ID thay vì toàn bộ đối tượng Hotel
    private String customerId; // Chỉ trả về ID thay vì toàn bộ đối tượng User
    private String bookingId; // Chỉ trả về ID đặt phòng
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isPositive; // Tính toán sẵn để client không phải xử lý
    private boolean isNegative; // Tính toán sẵn để client không phải xử lý

    // Phương thức chuyển từ Entity → DTO
    public static FeedbackDto toFeedbackDto(Feedback feedback) {
        if (feedback == null) {
            return null;
        }

        FeedbackDto dto = new FeedbackDto();
        dto.setFeedbackId(feedback.getFeedbackId());

        // Lấy thông tin khách sạn
        Hotel hotel = feedback.getHotel();
        if (hotel != null) {
            dto.setHotelId(hotel.getHotelId());
        }

        // Lấy thông tin khách hàng
        Customer customer = feedback.getCustomer();
        if (customer != null) {
            dto.setCustomerId(customer.getCustomerId());
        }

        // Lấy thông tin booking
        Booking booking = feedback.getBooking();
        if (booking != null) {
            dto.setBookingId(booking.getBookingId());
        }

        dto.setRating(feedback.getRating());
        dto.setContent(feedback.getContent());
        dto.setCreatedAt(feedback.getCreatedAt());
        dto.setUpdatedAt(feedback.getUpdatedAt());
        dto.setPositive(feedback.isPositiveFeedback());
        dto.setNegative(feedback.isNegativeFeedback());

        return dto;
    }
}