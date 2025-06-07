package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.PaymentDto;
import com.example.QuanLyKhachSan.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {
    /**
     * Tạo payment mới
     * @param paymentDto thông tin payment
     * @return PaymentDto đã được tạo
     */
    PaymentDto createPayment(PaymentDto paymentDto);

    /**
     * Lấy payment theo ID
     * @param paymentId ID của payment
     * @return PaymentDto
     */
    PaymentDto getPaymentById(String paymentId);

    /**
     * Lấy tất cả payments với phân trang
     * @param pageable thông tin phân trang (page, size, sort)
     * @return Page PaymentDto
     */
    Page<PaymentDto> getAllPayments(Pageable pageable);

    /**
     * Lấy payments theo booking ID với phân trang
     * @param bookingId ID của booking
     * @param pageable thông tin phân trang
     * @return Page PaymentDto
     */
    Page<PaymentDto> getPaymentsByBookingId(String bookingId, Pageable pageable);

    /**
     * Lấy payments theo trạng thái với phân trang
     * @param status trạng thái payment
     * @param pageable thông tin phân trang
     * @return Page PaymentDto
     */
    Page<PaymentDto> getPaymentsByStatus(PaymentStatus status, Pageable pageable);

    /**
     * Cập nhật payment
     * @param paymentId ID của payment cần cập nhật
     * @param paymentDto thông tin payment mới
     * @return PaymentDto đã được cập nhật
     */
    PaymentDto updatePayment(String paymentId, PaymentDto paymentDto);

    /**
     * Cập nhật trạng thái payment
     * @param paymentId ID của payment
     * @param status trạng thái mới
     * @return PaymentDto đã được cập nhật
     */
    PaymentDto updatePaymentStatus(String paymentId, PaymentStatus status);

    /**
     * Cập nhật provider response
     * @param paymentId ID của payment
     * @param providerResponse phản hồi từ provider
     * @return PaymentDto đã được cập nhật
     */
    PaymentDto updateProviderResponse(String paymentId, String providerResponse);

    /**
     * Xóa payment
     * @param paymentId ID của payment cần xóa
     */
    void deletePayment(String paymentId);

    /**
     * Kiểm tra payment có tồn tại không
     * @param paymentId ID của payment
     * @return true nếu tồn tại
     */
    boolean existsById(String paymentId);

    /**
     * Lấy payment theo transaction code
     * @param transactionCode mã giao dịch
     * @return PaymentDto
     */
    PaymentDto getPaymentByTransactionCode(String transactionCode);
}