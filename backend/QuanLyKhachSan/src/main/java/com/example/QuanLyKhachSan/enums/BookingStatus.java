package com.example.QuanLyKhachSan.enums;

public enum BookingStatus {
    PENDING,        // Đang chờ xác nhận (chưa thanh toán)
    PAID,           // Đã thanh toán toàn bộ
    CHECKED_IN,     // Đã nhận phòng
    CHECKED_OUT,    // Đã trả phòng
    CANCELLED,      // Đã hủy (trước khi nhận phòng)
    REFUNDED        // Đã hoàn tiền (sau khi hủy)
}
