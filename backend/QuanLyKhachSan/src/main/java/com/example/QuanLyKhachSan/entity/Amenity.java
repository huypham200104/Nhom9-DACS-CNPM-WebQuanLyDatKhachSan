package com.example.QuanLyKhachSan.entity;

import com.example.QuanLyKhachSan.enums.AmenityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "amenity")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private int amenityId; // int PRIMARY KEY

    @Column(name = "amenity_name", length = 255)
    private String amenityName; // nvarchar, e.g., 'Wi-Fi', 'TV', 'Minibar'

    @Column(name = "description", length = 255)
    private String description; // nvarchar, mô tả tiện nghi

    @Column(name = "amenity_status", length = 255)
    @Enumerated(EnumType.STRING)
    private AmenityStatus amenityStatus; // nvarchar ('available', 'unavailable')

    // Sửa lỗi: Mapping ngược từ Amenity về RoomType
    @ManyToMany(mappedBy = "amenities", fetch = FetchType.LAZY)
    private List<RoomType> roomTypes;

    public Amenity(Integer amenityId) {
        this.amenityId = amenityId;
    }
}