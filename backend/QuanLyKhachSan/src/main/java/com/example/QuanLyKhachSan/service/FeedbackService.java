package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.FeedbackDto;
import com.example.QuanLyKhachSan.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    FeedbackDto getFeedbackById(String feedbackId);
    List<FeedbackDto> getAllFeedbacks();
    FeedbackDto addFeedback(Feedback feedback);
    FeedbackDto updateFeedback(String feedbackId, Feedback feedback);
    void deleteFeedback(String feedbackId);
    List<FeedbackDto> getFeedbacksByHotelId(String hotelId);
    List<FeedbackDto> getFeedbacksByCustomerId(String customerId);
}
