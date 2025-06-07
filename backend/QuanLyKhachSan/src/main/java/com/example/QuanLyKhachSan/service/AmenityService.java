package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.AmenityDto;
import com.example.QuanLyKhachSan.entity.Amenity;

import java.util.List;

public interface AmenityService {
    AmenityDto getAmenityById(int amenityId);
    List<AmenityDto> getAllAmenities();
    AmenityDto addAmenity(Amenity amenity);
    AmenityDto updateAmenity(int amenityId, Amenity amenity);
    void deleteAmenity(int amenityId);

}
