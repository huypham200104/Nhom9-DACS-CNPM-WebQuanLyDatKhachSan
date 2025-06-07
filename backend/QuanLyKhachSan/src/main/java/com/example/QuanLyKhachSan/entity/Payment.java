package com.example.QuanLyKhachSan.entity;

import com.example.QuanLyKhachSan.enums.PaymentMethod;
import com.example.QuanLyKhachSan.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(name = "payment_id", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
        private String paymentId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(
                name = "booking_id",
                referencedColumnName = "booking_id",
                columnDefinition = "CHAR(36)",
                nullable = false
        )
        private Booking booking;

        @Column(name = "amount", nullable = false)
        private Long amount;

        @Column(name = "payment_date", nullable = false)
        private LocalDateTime paymentDate;

        @Column(name = "payment_details", length = 2000)  // Updated to a specific length
        private String paymentDetails;

        @Enumerated(EnumType.STRING)
        @Column(name = "payment_method", nullable = false) //     VNPAY,MOMO,CREDIT_CARD,BANK_TRANSFER,CASH,OTHER
        private PaymentMethod paymentMethod;

        @Column(name = "provider_response", length = 2000)  // Updated to a specific length
        private String providerResponse;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        private PaymentStatus status;
//        PENDING,       // Chờ xử lý
//        COMPLETED,     // Thanh toán thành công
//        FAILED,        // Thanh toán thất bại
//        REFUNDED,      // Đã hoàn tiền
//        PARTIALLY_REFUNDED // Hoàn tiền một phần

        @Column(name = "transaction_code", length = 100)
        private String transactionCode;
}
