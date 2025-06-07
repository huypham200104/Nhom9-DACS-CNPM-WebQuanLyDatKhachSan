package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.dto.FeedbackDto;
import com.example.QuanLyKhachSan.entity.Feedback;
import com.example.QuanLyKhachSan.service.impl.FeedbackServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = "*")
public class FeedBackController {
    @Autowired
    private FeedbackServiceImpl feedbackService;

    // Lấy feedback theo ID
    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDto> getFeedbackById(@PathVariable String feedbackId) {
        FeedbackDto feedbackDto = feedbackService.getFeedbackById(feedbackId);
        if (feedbackDto != null) {
            return ResponseEntity.ok(feedbackDto);
        }
        return ResponseEntity.notFound().build();
    }

    // Lấy tất cả feedback
    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {
        List<FeedbackDto> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    // Thêm mới feedback
    @PostMapping
    public ResponseEntity<?> addFeedback(@RequestBody Map<String, Object> feedbackRequest) {
        try {
            // Lấy dữ liệu từ request
            String hotelId = (String) feedbackRequest.get("hotelId");
            String customerId = (String) feedbackRequest.get("customerId");
            String bookingId = (String) feedbackRequest.get("bookingId");
            Integer rating = (Integer) feedbackRequest.get("rating");
            String content = (String) feedbackRequest.get("content");

            // Validate dữ liệu
            if (hotelId == null || customerId == null || bookingId == null ||
                    rating == null || content == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Thiếu thông tin bắt buộc"));
            }

            if (rating < 1 || rating > 5) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Điểm đánh giá phải từ 1 đến 5"));
            }

            // Kiểm tra xem booking đã có feedback chưa
            if (feedbackService.existsByBookingId(bookingId)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Đặt phòng này đã có đánh giá"));
            }

            // Gọi service để thêm feedback
            FeedbackDto savedFeedback = feedbackService.addFeedbackFromRequest(
                    hotelId, customerId, bookingId, rating, content);

            return ResponseEntity.ok(savedFeedback);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Lỗi khi thêm feedback: " + e.getMessage()));
        }
    }

    // Cập nhật feedback
    @PutMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDto> updateFeedback(@PathVariable String feedbackId, @RequestBody Feedback feedback) {
        FeedbackDto updatedFeedback = feedbackService.updateFeedback(feedbackId, feedback);
        if (updatedFeedback != null) {
            return ResponseEntity.ok(updatedFeedback);
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa feedback
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }

    // Lấy feedback theo hotelId
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbacksByHotelId(@PathVariable String hotelId) {
        List<FeedbackDto> feedbacks = feedbackService.getFeedbacksByHotelId(hotelId);
        return ResponseEntity.ok(feedbacks);
    }

    // Lấy feedback theo customerId
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbacksByCustomerId(@PathVariable String customerId) {
        List<FeedbackDto> feedbacks = feedbackService.getFeedbacksByCustomerId(customerId);
        return ResponseEntity.ok(feedbacks);
    }

    // Kiểm tra xem booking đã có feedback chưa
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Boolean> checkFeedbackByBookingId(@PathVariable String bookingId) {
        boolean exists = feedbackService.existsByBookingId(bookingId);
        return ResponseEntity.ok(exists);
    }
}