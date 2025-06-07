package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.ImageHotel;
import com.example.QuanLyKhachSan.enums.ApprovalStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {
    private String hotelId;
    private String hotelName;
    private Long hotelRating;
    private String city;
    private String district;
    private String ward;
    private String street;
    private String houseNumber;
    private ApprovalStatus approvalStatus;
    private List<ImageHotelDto> imageHotels;

    public static HotelDto fromEntity(Hotel hotel, List<ImageHotel> imageHotels) {
        HotelDto hotelDto = new HotelDto();
        hotelDto.setHotelId(hotel.getHotelId());
        hotelDto.setHotelName(hotel.getHotelName());
        hotelDto.setHotelRating(hotel.getHotelRating());
        hotelDto.setCity(hotel.getCity());
        hotelDto.setDistrict(hotel.getDistrict());
        hotelDto.setWard(hotel.getWard());
        hotelDto.setStreet(hotel.getStreet());
        hotelDto.setHouseNumber(hotel.getHouseNumber());
        hotelDto.setApprovalStatus(hotel.getApprovalStatus());

        if (imageHotels != null) {
            hotelDto.setImageHotels(imageHotels.stream()
                    .map(imageHotel -> {
                        try {
                            return ImageHotelDto.fromEntity(imageHotel);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to read image file: " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList()));
        } else {
            hotelDto.setImageHotels(new ArrayList<>());
        }

        return hotelDto;
    }
}