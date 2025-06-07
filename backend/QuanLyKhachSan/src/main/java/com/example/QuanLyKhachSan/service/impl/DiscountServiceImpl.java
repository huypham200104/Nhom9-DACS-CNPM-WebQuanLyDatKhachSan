package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.DiscountDto;
import com.example.QuanLyKhachSan.entity.Booking;
import com.example.QuanLyKhachSan.entity.Discount;
import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.repository.DiscountRepository;
import com.example.QuanLyKhachSan.repository.HotelRepository;
import com.example.QuanLyKhachSan.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public DiscountDto addDiscount(Discount discount, String hotelId) {
        // Kiem tra discount code ton tai chua
        if (discountRepository.existsByDiscountCode(discount.getDiscountCode())) {
            throw new RuntimeException("Discount code " + discount.getDiscountCode() + " already exists");
        }

        // Lưu discount vào cơ sở dữ liệu
        discountRepository.save(discount);

        return new DiscountDto().toDto(discount, hotelId);
    }

    @Override
    public DiscountDto updateDiscount(Discount discount, String hotelId) {
        // Kiểm tra nếu discountId đã tồn tại
        if (!discountRepository.existsById(discount.getDiscountId())) {
            throw new RuntimeException("Discount with ID " + discount.getDiscountId() + " is not found");
        }

        // Nếu tồn tại → cập nhật
        Discount updatedDiscount = discountRepository.save(discount);
        return new DiscountDto().toDto(updatedDiscount, hotelId);
    }

    @Override
    public DiscountDto getDiscountById(String discountId) {
        // Kiểm tra nếu discountId đã tồn tại
        if (!discountRepository.existsById(discountId)) {
            throw new RuntimeException("Discount with ID " + discountId + " is not found");
        }

        // Nếu tồn tại → lấy thông tin
        Discount discount = discountRepository.findById(discountId).orElseThrow(
                () -> new RuntimeException("Discount not found"));

        return new DiscountDto().toDto(discount, discount.getHotel().getHotelId());
    }

    @Override
    public void deleteDiscount(String discountId) {
        Discount discount = discountRepository.findById(discountId).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("Discount not found"));
        discountRepository.delete(discount);
    }
    @Override
    public DiscountDto getDiscountByCode(String discountCode) {
        // Kiểm tra nếu discountCode đã tồn tại
        if (!discountRepository.existsByDiscountCode(discountCode)) {
            throw new RuntimeException("Discount with code " + discountCode + " is not found");
        }

        // Nếu tồn tại → lấy thông tin
        Discount discount = discountRepository.findByDiscountCode(discountCode).orElseThrow(
                () -> new RuntimeException("Discount not found"));
        return new DiscountDto().toDto(discount, discount.getHotel().getHotelId());
    }

    @Override
    public List<DiscountDto> getDiscountsByHotelId(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                () -> new RuntimeException("Hotel not found"));
        List<Discount> discounts = discountRepository.findByHotel(hotel);
        return discounts.stream()
                .map(discount -> new DiscountDto().toDto(discount, hotelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscountDto> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream()
                .map(discount -> new DiscountDto().toDto(discount, discount.getHotel().getHotelId()))
                .collect(Collectors.toList());
    }
    @Override
    public Discount validateDiscount(String discountCode, Long bookingAmount, String hotelId) {
        // Kiểm tra mã giảm giá tồn tại
        if (!discountRepository.existsByDiscountCode(discountCode)) {
            throw new CustomExceptions.InvalidInputException("Discount with code " + discountCode + " is not found");
        }

        Discount discount = discountRepository.findByDiscountCode(discountCode).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("Discount not found"));

        // Kiểm tra xem mã giảm giá có thuộc khách sạn không
        if (!discount.getHotel().getHotelId().equals(hotelId)) {
            throw new CustomExceptions.InvalidInputException(
                    "Discount code " + discountCode + " is not applicable for the specified hotel");
        }

        // Kiểm tra ngày hết hạn
        LocalDate currentDate = LocalDate.now();
        if (discount.getStartDate() != null && currentDate.isBefore(discount.getStartDate())) {
            throw new CustomExceptions.InvalidInputException(
                    "Discount code " + discountCode + " is not yet valid (valid from " + discount.getStartDate() + ")");
        }
        if (discount.getEndDate() != null && currentDate.isAfter(discount.getEndDate())) {
            throw new CustomExceptions.InvalidInputException(
                    "Discount code " + discountCode + " has expired (expired on " + discount.getEndDate() + ")");
        }

        // Kiểm tra số lượt sử dụng còn lại
        if (discount.getNumberOfUses() != null && discount.getUsedCount() != null &&
                discount.getUsedCount() >= discount.getNumberOfUses()) {
            throw new CustomExceptions.InvalidInputException(
                    "Discount code " + discountCode + " has reached its usage limit");
        }

        // Kiểm tra số tiền tối thiểu
        if (discount.getMinimumBookingAmount() != null && bookingAmount < discount.getMinimumBookingAmount()) {
            throw new CustomExceptions.InvalidInputException(
                    "Booking amount " + bookingAmount + " is less than minimum required " +
                            discount.getMinimumBookingAmount() + " for discount code " + discountCode);
        }

        return discount;
    }
}
