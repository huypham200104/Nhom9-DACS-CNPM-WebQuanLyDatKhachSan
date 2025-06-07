package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface HotelRepository extends JpaRepository<Hotel, String> , JpaSpecificationExecutor<Hotel> {
    Optional<Hotel> findByHotelName(String hotelName);

    List<Hotel> findByCity(String city);

    Optional<Hotel> findByDistrict(String district);
        @Query("SELECT h FROM Hotel h WHERE " +
                "h.city = :city AND " +
                "h.district = :district AND " +
                "h.ward = :ward AND " +
                "h.street = :street AND " +
                "h.houseNumber = :houseNumber")
        Optional<Hotel> findByFullAddress(
                @Param("city") String city,
                @Param("district") String district,
                @Param("ward") String ward,
                @Param("street") String street,
                @Param("houseNumber") String houseNumber
        );

    Optional<Hotel> findByWard(String ward);

    Optional<Hotel> findByStreet(String street);
    boolean existsByCityAndDistrictAndWardAndStreetAndHouseNumber(
            String city,
            String district,
            String ward,
            String street,
            String houseNumber
    );
    boolean existsByHotelName(String hotelName);

    }
