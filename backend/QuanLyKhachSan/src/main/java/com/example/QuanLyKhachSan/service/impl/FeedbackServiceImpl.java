package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.FeedbackDto;
import com.example.QuanLyKhachSan.entity.Feedback;
import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.Booking;
import com.example.QuanLyKhachSan.repository.FeedBackRepository;
import com.example.QuanLyKhachSan.repository.HotelRepository;
import com.example.QuanLyKhachSan.repository.CustomerRepository;
import com.example.QuanLyKhachSan.repository.BookingRepository;
import com.example.QuanLyKhachSan.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public FeedbackDto getFeedbackById(String feedbackId) {
        Feedback feedback = feedBackRepository.findById(feedbackId).orElse(null);
        if (feedback != null) {
            return FeedbackDto.toFeedbackDto(feedback);
        }
        return null;
    }

    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        List<Feedback> feedbacks = feedBackRepository.findAll();
        return feedbacks.stream()
                .map(FeedbackDto::toFeedbackDto)
                .toList();
    }

    @Override
    public FeedbackDto addFeedback(Feedback feedback) {
        Feedback savedFeedback = feedBackRepository.save(feedback);
        return FeedbackDto.toFeedbackDto(savedFeedback);
    }

    // Method mới để xử lý request từ frontend
    public FeedbackDto addFeedbackFromRequest(String hotelId, String customerId,
                                              String bookingId, Integer rating, String content) {
        try {
            // Tìm các entity từ ID
            Hotel hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn với ID: " + hotelId));

            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + customerId));

            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy booking với ID: " + bookingId));

            // Tạo feedback mới
            Feedback feedback = new Feedback();
            feedback.setHotel(hotel);
            feedback.setCustomer(customer);
            feedback.setBooking(booking);
            feedback.setRating(rating);
            feedback.setContent(content);
            feedback.setCreatedAt(LocalDateTime.now());
            feedback.setUpdatedAt(LocalDateTime.now());

            // Lưu feedback
            Feedback savedFeedback = feedBackRepository.save(feedback);
            return FeedbackDto.toFeedbackDto(savedFeedback);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo feedback: " + e.getMessage(), e);
        }
    }

    @Override
    public FeedbackDto updateFeedback(String feedbackId, Feedback feedback) {
        Feedback existingFeedback = feedBackRepository.findById(feedbackId).orElse(null);
        if (existingFeedback != null) {
            existingFeedback.setRating(feedback.getRating());
            existingFeedback.setContent(feedback.getContent());
            existingFeedback.setUpdatedAt(LocalDateTime.now());
            Feedback updatedFeedback = feedBackRepository.save(existingFeedback);
            return FeedbackDto.toFeedbackDto(updatedFeedback);
        }
        return null;
    }

    @Override
    public void deleteFeedback(String feedbackId) {
        Feedback feedback = feedBackRepository.findById(feedbackId).orElse(null);
        if (feedback != null) {
            feedBackRepository.delete(feedback);
        }
    }

    @Override
    public List<FeedbackDto> getFeedbacksByHotelId(String hotelId) {
        List<Feedback> feedbacks = feedBackRepository.findByHotel_HotelId(hotelId);
        return feedbacks.stream()
                .map(FeedbackDto::toFeedbackDto)
                .toList();
    }

    @Override
    public List<FeedbackDto> getFeedbacksByCustomerId(String customerId) {
        List<Feedback> feedbacks = feedBackRepository.findByCustomer_CustomerId(customerId);
        return feedbacks.stream()
                .map(FeedbackDto::toFeedbackDto)
                .toList();
    }

    public boolean existsByBookingId(String bookingId) {
        return feedBackRepository.existsByBookingId(bookingId);
    }
}