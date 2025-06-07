package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Payment;
import com.example.QuanLyKhachSan.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    /**
     * Tìm payments theo booking ID
     */
    List<Payment> findByBooking_BookingId(String bookingId);

    /**
     * Tìm payments theo booking ID với pagination
     */
    Page<Payment> findByBooking_BookingId(String bookingId, Pageable pageable);

    /**
     * Tìm payments theo status
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Tìm payments theo status với pagination
     */
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);

    /**
     * Tìm payment theo transaction code
     */
    Optional<Payment> findByTransactionCode(String transactionCode);

    /**
     * Tìm payments theo khoảng thời gian
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    /**
     * Tìm payments theo khoảng thời gian với pagination
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    Page<Payment> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);

    /**
     * Tìm payments theo booking ID và status
     */
    List<Payment> findByBooking_BookingIdAndStatus(String bookingId, PaymentStatus status);

    /**
     * Tìm payments theo booking ID và status với pagination
     */
    Page<Payment> findByBooking_BookingIdAndStatus(String bookingId, PaymentStatus status, Pageable pageable);

    /**
     * Tìm payments theo payment method
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :paymentMethod")
    List<Payment> findByPaymentMethod(@Param("paymentMethod") String paymentMethod);

    /**
     * Tìm payments theo payment method với pagination
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :paymentMethod")
    Page<Payment> findByPaymentMethod(@Param("paymentMethod") String paymentMethod, Pageable pageable);

    /**
     * Đếm số payments theo status
     */
    long countByStatus(PaymentStatus status);

    /**
     * Tính tổng amount theo status
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = :status")
    Long sumAmountByStatus(@Param("status") PaymentStatus status);

    /**
     * Tìm payments gần đây theo booking ID
     */
    @Query("SELECT p FROM Payment p WHERE p.booking.bookingId = :bookingId ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPaymentsByBookingId(@Param("bookingId") String bookingId);

    /**
     * Tìm payments gần đây theo booking ID với pagination
     */
    @Query("SELECT p FROM Payment p WHERE p.booking.bookingId = :bookingId ORDER BY p.paymentDate DESC")
    Page<Payment> findRecentPaymentsByBookingId(@Param("bookingId") String bookingId, Pageable pageable);

    /**
     * Kiểm tra xem booking có payment thành công không
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p " +
            "WHERE p.booking.bookingId = :bookingId AND p.status = 'COMPLETED'")
    boolean existsCompletedPaymentByBookingId(@Param("bookingId") String bookingId);

    /**
     * Tìm payments theo multiple status với pagination
     */
    @Query("SELECT p FROM Payment p WHERE p.status IN :statuses")
    Page<Payment> findByStatusIn(@Param("statuses") List<PaymentStatus> statuses, Pageable pageable);

    /**
     * Tìm payments theo booking ID và multiple status với pagination
     */
    @Query("SELECT p FROM Payment p WHERE p.booking.bookingId = :bookingId AND p.status IN :statuses")
    Page<Payment> findByBooking_BookingIdAndStatusIn(@Param("bookingId") String bookingId,
                                                     @Param("statuses") List<PaymentStatus> statuses,
                                                     Pageable pageable);

    /**
     * Tìm payments theo amount range với pagination
     */
    @Query("SELECT p FROM Payment p WHERE p.amount BETWEEN :minAmount AND :maxAmount")
    Page<Payment> findByAmountBetween(@Param("minAmount") Long minAmount,
                                      @Param("maxAmount") Long maxAmount,
                                      Pageable pageable);
    /**
     * Tim bookingId nay da ton tai chua
     */
    @Query("SELECT COUNT(p) > 0 FROM Payment p WHERE p.booking.bookingId = :bookingId")
    boolean existsByBooking_BookingId(@Param("bookingId") String bookingId);
}