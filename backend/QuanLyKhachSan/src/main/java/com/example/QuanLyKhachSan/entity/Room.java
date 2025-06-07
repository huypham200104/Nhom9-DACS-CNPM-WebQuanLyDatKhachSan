package com.example.QuanLyKhachSan.entity;

import com.example.QuanLyKhachSan.enums.BedType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(name = "room_id", columnDefinition = "CHAR(36)")
        private String roomId;

        @Column(name = "created_at")
        private LocalDate createdAt;

        @Column(name = "description", columnDefinition = "TEXT")
        private String description;

        @Column(name = "max_guests", nullable = false)
        private Integer maxGuests;

        @Column(name = "price", nullable = false)
        private Long price;

        @Column(name = "room_size")
        private Long roomSize;

        @Column(name = "room_status", length = 20)
        private String roomStatus;

        @Column(name = "updated_at")
        private LocalDate updatedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "hotel_id")
        private Hotel hotel;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "room_type_id")
        private RoomType roomType;

        @Enumerated(EnumType.STRING)
        @Column(name = "bed_type")
        private BedType bedType;

        @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
        @JsonManagedReference
        private List<ImageRoom> imageRooms;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "booking_room",
                joinColumns = @JoinColumn(name = "room_id"),
                inverseJoinColumns = @JoinColumn(name = "booking_id")
        )
        private List<Booking> bookings = new ArrayList<>();
}