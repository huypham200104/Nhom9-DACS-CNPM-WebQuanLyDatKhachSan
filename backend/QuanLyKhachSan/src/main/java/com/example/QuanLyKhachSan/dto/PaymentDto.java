package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Booking;
import com.example.QuanLyKhachSan.entity.Payment;
import com.example.QuanLyKhachSan.enums.PaymentMethod;
import com.example.QuanLyKhachSan.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private String paymentId;
    private String bookingId; // bookingId thay vì Booking object
    private Long amount;
    private LocalDateTime paymentDate;
    private String paymentDetails;
    private PaymentMethod paymentMethod;
    private String providerResponse;
    private PaymentStatus status;
    private String transactionCode;

    /**
     * Convert entity -> DTO
     */
    public static PaymentDto toPaymentDto(Payment payment){
        if (payment == null) return null;

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(payment.getPaymentId());
        paymentDto.setBookingId(payment.getBooking() != null ? payment.getBooking().getBookingId() : null);
        paymentDto.setAmount(payment.getAmount());
        paymentDto.setPaymentDate(payment.getPaymentDate());
        paymentDto.setPaymentDetails(payment.getPaymentDetails());
        paymentDto.setPaymentMethod(payment.getPaymentMethod());
        paymentDto.setProviderResponse(payment.getProviderResponse());
        paymentDto.setStatus(payment.getStatus());
        paymentDto.setTransactionCode(payment.getTransactionCode());
        return paymentDto;
    }

    /**
     * Convert DTO -> entity
     */
    public static Payment toPaymentEntity(PaymentDto dto, Booking booking) {
        if (dto == null || booking == null) return null;

        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId()); // optional - nếu là tạo mới thì có thể để null
        payment.setBooking(booking); // booking phải được lấy từ DB hoặc service bên ngoài
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setPaymentDetails(dto.getPaymentDetails());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setProviderResponse(dto.getProviderResponse());
        payment.setStatus(dto.getStatus());
        payment.setTransactionCode(dto.getTransactionCode());
        return payment;
    }
}
