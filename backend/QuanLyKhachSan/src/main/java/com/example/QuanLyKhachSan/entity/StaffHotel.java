package com.example.QuanLyKhachSan.entity;

import com.example.QuanLyKhachSan.enums.PositionHotel;
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
@Table(name = "staff_hotel")
public class StaffHotel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "staff_hotel_id", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private String staffHotelId;

    // Không sử dụng @JoinColumn mà tự quản lý khóa ngoại
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id")
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", columnDefinition = "ENUM('MANAGER','RECEPTIONIST')")
    private PositionHotel position;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate; // null if still working
}
