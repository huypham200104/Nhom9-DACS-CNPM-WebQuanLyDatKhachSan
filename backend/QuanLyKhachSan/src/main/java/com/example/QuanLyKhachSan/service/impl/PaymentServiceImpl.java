package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.PaymentDto;
import com.example.QuanLyKhachSan.entity.Booking;
import com.example.QuanLyKhachSan.entity.Payment;
import com.example.QuanLyKhachSan.enums.PaymentStatus;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.repository.BookingRepository;
import com.example.QuanLyKhachSan.repository.PaymentRepository;
import com.example.QuanLyKhachSan.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final BookingRepository bookingRepository;

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        validatePaymentDto(paymentDto, true);

        if (paymentRepository.existsByBooking_BookingId(paymentDto.getBookingId())) {
            throw new IllegalArgumentException("Payment for this booking already exists");
        }

        Booking booking = bookingRepository.findById(paymentDto.getBookingId())
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                        "Booking with ID " + paymentDto.getBookingId() + " not found"));

        if (paymentDto.getPaymentDate() == null) {
            paymentDto.setPaymentDate(LocalDateTime.now());
        }

        if (paymentDto.getStatus() == null) {
            paymentDto.setStatus(PaymentStatus.PENDING);
        }

        Payment savedPayment = paymentRepository.save(PaymentDto.toPaymentEntity(paymentDto, booking));

        log.info("Payment created successfully with ID: {}", savedPayment.getPaymentId());
        return PaymentDto.toPaymentDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(String paymentId) {
        validatePaymentId(paymentId);
        return PaymentDto.toPaymentDto(findPaymentById(paymentId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDto> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(PaymentDto::toPaymentDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDto> getPaymentsByBookingId(String bookingId, Pageable pageable) {
        validateBookingId(bookingId);
        if (!bookingRepository.existsById(bookingId)) {
            throw new CustomExceptions.ResourceNotFoundException("Booking with ID " + bookingId + " not found");
        }
        return paymentRepository.findByBooking_BookingId(bookingId, pageable).map(PaymentDto::toPaymentDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDto> getPaymentsByStatus(PaymentStatus status, Pageable pageable) {
        if (status == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }
        return paymentRepository.findByStatus(status, pageable).map(PaymentDto::toPaymentDto);
    }

    @Override
    public PaymentDto updatePayment(String paymentId, PaymentDto paymentDto) {
        validatePaymentId(paymentId);
        validatePaymentDto(paymentDto, false);

        Payment existing = findPaymentById(paymentId);

        if (paymentDto.getAmount() != null && paymentDto.getAmount() > 0) existing.setAmount(paymentDto.getAmount());
        if (paymentDto.getPaymentDate() != null) existing.setPaymentDate(paymentDto.getPaymentDate());
        if (paymentDto.getPaymentDetails() != null) existing.setPaymentDetails(paymentDto.getPaymentDetails());
        if (paymentDto.getPaymentMethod() != null) existing.setPaymentMethod(paymentDto.getPaymentMethod());
        if (paymentDto.getProviderResponse() != null) existing.setProviderResponse(paymentDto.getProviderResponse());
        if (paymentDto.getStatus() != null) existing.setStatus(paymentDto.getStatus());
        if (paymentDto.getTransactionCode() != null) existing.setTransactionCode(paymentDto.getTransactionCode());

        if (paymentDto.getBookingId() != null &&
                !paymentDto.getBookingId().equals(existing.getBooking().getBookingId())) {
            Booking newBooking = bookingRepository.findById(paymentDto.getBookingId())
                    .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                            "Booking with ID " + paymentDto.getBookingId() + " not found"));
            existing.setBooking(newBooking);
        }

        return PaymentDto.toPaymentDto(paymentRepository.save(existing));
    }

    @Override
    public PaymentDto updatePaymentStatus(String paymentId, PaymentStatus status) {
        validatePaymentId(paymentId);
        if (status == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }

        Payment payment = findPaymentById(paymentId);
        payment.setStatus(status);

        if (status == PaymentStatus.COMPLETED && payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }

        return PaymentDto.toPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto updateProviderResponse(String paymentId, String providerResponse) {
        validatePaymentId(paymentId);
        Payment payment = findPaymentById(paymentId);
        payment.setProviderResponse(providerResponse);
        return PaymentDto.toPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public void deletePayment(String paymentId) {
        validatePaymentId(paymentId);
        if (!paymentRepository.existsById(paymentId)) {
            throw new CustomExceptions.ResourceNotFoundException("Payment with ID " + paymentId + " not found");
        }
        paymentRepository.deleteById(paymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String paymentId) {
        return paymentId != null && !paymentId.trim().isEmpty() && paymentRepository.existsById(paymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentByTransactionCode(String transactionCode) {
        if (transactionCode == null || transactionCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction code cannot be null or empty");
        }

        return paymentRepository.findByTransactionCode(transactionCode)
                .map(PaymentDto::toPaymentDto)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                        "Payment with transaction code " + transactionCode + " not found"));
    }

    // ======= Helper Methods =======
    private void validatePaymentId(String paymentId) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment ID cannot be null or empty");
        }
    }

    private void validateBookingId(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }
    }

    private void validatePaymentDto(PaymentDto dto, boolean isCreating) {
        if (dto == null) {
            throw new IllegalArgumentException("Payment data cannot be null");
        }
        if (isCreating) {
            if (dto.getBookingId() == null || dto.getBookingId().trim().isEmpty()) {
                throw new IllegalArgumentException("Booking ID is required");
            }
            if (dto.getAmount() == null || dto.getAmount() <= 0) {
                throw new IllegalArgumentException("Payment amount must be greater than 0");
            }
            if (dto.getPaymentMethod() == null) {
                throw new IllegalArgumentException("Payment method is required");
            }
        }
    }

    private Payment findPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                        "Payment with ID " + paymentId + " not found"));
    }
}
