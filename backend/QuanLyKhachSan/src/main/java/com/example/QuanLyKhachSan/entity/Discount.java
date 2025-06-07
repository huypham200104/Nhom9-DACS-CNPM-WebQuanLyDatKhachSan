package com.example.QuanLyKhachSan.entity;

import com.example.QuanLyKhachSan.enums.DiscountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "discount_id", columnDefinition = "CHAR(36)")
    private String discountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id")
    private Hotel hotel;

    @Column(name = "name_discount", length = 255)
    private String nameDiscount;

    @Column(name = "percentage")
    private Integer percentage;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "number_of_uses")
    private Integer numberOfUses;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "discount_code", length = 255)
    private String discountCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DiscountStatus status;

    @Column(name = "minimum_booking_amount")
    private Long minimumBookingAmount;

    public Discount(String discountId, String nameDiscount, Integer percentage, LocalDate startDate, LocalDate endDate,
                   Integer numberOfUses, Integer usedCount, String discountCode, DiscountStatus status,
                   Long minimumBookingAmount) {
        this.discountId = discountId;
        this.nameDiscount = nameDiscount;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfUses = numberOfUses;
        this.usedCount = usedCount;
        this.discountCode = discountCode;
        this.status = status;
        this.minimumBookingAmount = minimumBookingAmount;
    }
}