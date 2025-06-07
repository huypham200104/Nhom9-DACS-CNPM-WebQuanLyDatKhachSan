package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Discount;
import com.example.QuanLyKhachSan.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, String> {
    boolean existsByDiscountCode(String discountCode);

    Optional<Discount> findByDiscountCode(String discountCode);

    List<Discount> findByHotel(Hotel hotel);
}
