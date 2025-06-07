    package com.example.QuanLyKhachSan.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.hibernate.annotations.GenericGenerator;
    import org.springframework.web.multipart.MultipartFile;

    import java.util.List;
    import java.util.UUID;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "image_hotel")
    public class ImageHotel {
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(name = "image_hotel_id", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
        private String imageHotelId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id")
        private Hotel hotel; // varchar, liên kết với hotel_id từ bảng hotel

        @Column(name = "image_hotel_url")
        private String imageHotelUrl;

    }