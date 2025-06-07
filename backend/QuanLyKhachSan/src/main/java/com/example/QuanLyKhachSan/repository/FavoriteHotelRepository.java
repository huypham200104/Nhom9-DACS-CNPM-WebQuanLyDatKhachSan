package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.FavoriteHotel;
import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteHotelRepository extends JpaRepository<FavoriteHotel, String> {
    boolean existsByCustomerAndHotel(Customer customer, Hotel hotel); // Nếu có phương thức này

    FavoriteHotel findByCustomerAndHotel(Customer customer, Hotel hotel); // Lấy yêu thích của khách hàng theo id

    List<FavoriteHotel> findByCustomer(Customer customer); // Lấy danh sách yêu thích của khách hàng
}
