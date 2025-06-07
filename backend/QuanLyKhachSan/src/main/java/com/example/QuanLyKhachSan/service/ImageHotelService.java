package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.ImageHotelDto;
import com.example.QuanLyKhachSan.entity.ImageHotel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageHotelService {
    List<ImageHotelDto> addImageToHotel(String hotelId, List<MultipartFile> files);
    List<ImageHotelDto> updateImageToHotel(String hotelId, List<MultipartFile> files);
    List<ImageHotelDto> getImageByHotelId(String HotelId);
    void deleteImageByHotelId(String hotelId);

}
