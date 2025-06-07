package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.StaffHotel;
import com.example.QuanLyKhachSan.enums.PositionHotel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
public class StaffHotelWithHotelNameDto extends StaffHotelDto {
    private String hotelName;
    private String email;

    public StaffHotelWithHotelNameDto(String staffHotelId, Hotel hotel, String hotelName, String email, PositionHotel position, LocalDate startDate, LocalDate endDate) {
        super(staffHotelId, position, hotel, startDate, endDate);
        this.hotelName = hotelName;
        this.email = email;
    }


    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    public static StaffHotelWithHotelNameDto toStaffHotelWithHotelNameDto(StaffHotel staffHotel, String hotelName) {
        return new StaffHotelWithHotelNameDto(
                staffHotel.getStaffHotelId(),
                staffHotel.getHotel(),
                hotelName,
                staffHotel.getUser().getEmail(),
                staffHotel.getPosition(),
                staffHotel.getStartDate(),
                staffHotel.getEndDate()
        );
    }
    public static List<StaffHotelWithHotelNameDto> toStaffHotelWithHotelNameDtoList(List<StaffHotel> staffHotels) {
        // Tra ve danh sach Staff voi ten khach san va email
        return staffHotels.stream()
                .map(staffHotel -> toStaffHotelWithHotelNameDto(staffHotel, staffHotel.getHotel().getHotelName()))
                .toList();
    }
}
