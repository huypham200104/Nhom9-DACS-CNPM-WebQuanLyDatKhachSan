package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.StaffHotel;
import com.example.QuanLyKhachSan.enums.PositionHotel;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data

public class StaffHotelDto {
    private String staffHotelId;
    private String hotelId;
    private PositionHotel position;
    private LocalDate startDate;
    private LocalDate endDate;      // Ngày kết thúc (nếu có)

    public StaffHotelDto(String staffHotelId, PositionHotel position, Hotel hotel, LocalDate startDate, LocalDate endDate) {
        this.staffHotelId = staffHotelId;
        this.position = position;
        this.hotelId = hotel.getHotelId();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static StaffHotelDto toStaffHotelDto(StaffHotel staffHotel) {
        return new StaffHotelDto(
                staffHotel.getStaffHotelId(),
                staffHotel.getPosition(),
                staffHotel.getHotel(),
                staffHotel.getStartDate(),
                staffHotel.getEndDate()
        );
    }
    public static List<StaffHotelDto> toStaffHotelDtoList(List<StaffHotel> staffHotels) {
        return staffHotels.stream()
                .map(StaffHotelDto::toStaffHotelDto)
                .collect(Collectors.toList());
    }

}