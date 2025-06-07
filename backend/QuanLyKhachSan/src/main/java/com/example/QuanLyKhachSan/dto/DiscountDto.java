package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Discount;
import com.example.QuanLyKhachSan.enums.DiscountStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DiscountDto {
    private String discountId;
    private String hotelId;       // ID khách sạn áp dụng
//    private String hotelName;     // Tên khách sạn (nếu cần)
    private String nameDiscount;
    private Integer percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfUses; // Số lần sử dụng tối đa
    private Integer usedCount;    // Số lần đã dùng
    private String discountCode;
    private DiscountStatus status;
    private Long minimumBookingAmount; // Số tiền tối thiểu để áp dụng

    // Convert từ Entity -> DTO
    // Convert từ Entity -> DTO (Gộp hai phương thức thành một)
    public static DiscountDto toDto(Discount discount, String hotelId) {
        DiscountDto dto = new DiscountDto();
        dto.setDiscountId(discount.getDiscountId());
        dto.setNameDiscount(discount.getNameDiscount());
        dto.setPercentage(discount.getPercentage());
        dto.setStartDate(discount.getStartDate());
        dto.setEndDate(discount.getEndDate());
        dto.setNumberOfUses(discount.getNumberOfUses());
        dto.setUsedCount(discount.getUsedCount());
        dto.setDiscountCode(discount.getDiscountCode());
        dto.setStatus(discount.getStatus());
        dto.setMinimumBookingAmount(discount.getMinimumBookingAmount());

        if (discount.getHotel() != null) {
            dto.setHotelId(discount.getHotel().getHotelId());

        }

        return dto;
    }

}