package com.example.QuanLyKhachSan.enums;

public enum PaymentStatus {
    PENDING,       // Chờ xử lý
    COMPLETED,     // Thanh toán thành công
    FAILED,        // Thanh toán thất bại
    REFUNDED,      // Đã hoàn tiền
    PARTIALLY_REFUNDED // Hoàn tiền một phần
}
