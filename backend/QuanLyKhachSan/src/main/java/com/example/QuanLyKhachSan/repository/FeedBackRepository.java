package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findByHotel_HotelId(String hotelId);

    List<Feedback> findByCustomer_CustomerId(String customerId);

    @Query("SELECT COUNT(f) > 0 FROM Feedback f WHERE f.booking.bookingId = :bookingId")
    boolean existsByBookingId(String bookingId);
}
