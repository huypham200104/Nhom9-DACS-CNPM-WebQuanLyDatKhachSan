package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.DiscountDto;
import com.example.QuanLyKhachSan.entity.Discount;

import java.util.List;

public interface DiscountService {
    DiscountDto addDiscount(Discount discount, String hotelId);
    DiscountDto updateDiscount(Discount discount, String hotelId);
    DiscountDto getDiscountById(String discountId);
    List<DiscountDto> getAllDiscounts();
    void deleteDiscount(String discountId);
    DiscountDto getDiscountByCode(String discountCode);
    List<DiscountDto> getDiscountsByHotelId(String hotelId);
    Discount validateDiscount(String discountCode, Long bookingAmount, String hotelId);

}
