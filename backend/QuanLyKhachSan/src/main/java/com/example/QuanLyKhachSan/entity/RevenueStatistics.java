package com.example.QuanLyKhachSan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "revenue_statistics")
public class RevenueStatistics {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "statistic_id", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private String statisticId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id")
    private Hotel hotel;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "total_revenue", nullable = false)
    private Long totalRevenue;  // Stored as Long, stored as the smallest unit (e.g., cents)

    @Column(name = "total_bookings")
    private int totalBookings;

    @Column(name = "average_rating")
    private Long averageRating;  // Stored as Long, can represent a fixed-point value (e.g., 495 for 4.95)

    @Column(name = "occupancy_rate")
    private Long occupancyRate;  // Stored as Long, representing percentage (e.g., 9999 for 99.99%)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;
}
