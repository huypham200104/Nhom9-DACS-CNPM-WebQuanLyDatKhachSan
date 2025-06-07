package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.HotelDto;
import com.example.QuanLyKhachSan.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {
    HotelDto getHotelById(String hotelId);
    Page<HotelDto> getAllHotels(int page, int size);
    HotelDto createHotel(Hotel hotel, List<MultipartFile> imageFiles);
    HotelDto updateHotel(String hotelId, Hotel hotel, List<MultipartFile> imageFiles);
    void deleteHotel(String hotelId);
    HotelDto approveHotel(String hotelId);
    HotelDto rejectHotel(String hotelId);
    HotelDto getHotelByName(String hotelName);
    Page<HotelDto> getHotelByCity(String city, int page, int size);
    Page<HotelDto> getHotelByDistrict(String district, int page, int size);
    Page<HotelDto> getHotelByWard(String ward, int page, int size);
    Page<HotelDto> getHotelByStreet(String street, int page, int size);
    List<HotelDto> searchHotels(String city, String district, String ward, String street, String houseNumber);
    HotelDto getHotelByUserId(String userId);
    List<String> getAllHotelNames();

}
