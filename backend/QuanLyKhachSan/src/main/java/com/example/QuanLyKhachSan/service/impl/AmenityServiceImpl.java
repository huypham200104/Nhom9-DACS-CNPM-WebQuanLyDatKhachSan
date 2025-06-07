package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.AmenityDto;
import com.example.QuanLyKhachSan.entity.Amenity;
import com.example.QuanLyKhachSan.enums.AmenityStatus;
import com.example.QuanLyKhachSan.repository.AmenityRepository;
import com.example.QuanLyKhachSan.service.AmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AmenityServiceImpl implements AmenityService {
    @Autowired
    private AmenityRepository amenityRepository;

    @Override
    public AmenityDto getAmenityById(int amenityId) {
        Amenity amenity = amenityRepository.findById(amenityId).orElseThrow(() -> new RuntimeException("Amenity not found"));
        return AmenityDto.toDto(amenity);
    }

    @Override
    public List<AmenityDto> getAllAmenities() {
        List<Amenity> amenities = amenityRepository.findAll();
        return amenities.stream().map(amenity -> {
            AmenityDto amenityDto = new AmenityDto();
            amenityDto.setAmenityId(amenity.getAmenityId());
            amenityDto.setAmenityName(amenity.getAmenityName());
            amenityDto.setDescription(amenity.getDescription());
            amenityDto.setAmenityStatus(amenity.getAmenityStatus());
            return amenityDto;
        }).toList();
    }

    @Override
    public AmenityDto addAmenity(Amenity amenity) {
        // Kiểm tra nếu amenityId đã tồn tại
        if (amenity.getAmenityId() > 0 && amenityRepository.existsById(amenity.getAmenityId())) {
            throw new RuntimeException("Amenity already exists");
        }

        // Hoặc kiểm tra bằng tên nếu cần
        if (amenityRepository.existsByAmenityName(amenity.getAmenityName())) {
            throw new RuntimeException("Amenity with this name already exists");
        }

        amenity.setAmenityStatus(AmenityStatus.AVAILABLE);
        Amenity savedAmenity = amenityRepository.save(amenity);
        return AmenityDto.toDto(savedAmenity);
    }

    @Override
    public AmenityDto updateAmenity(int amenityId, Amenity amenity) {
        Amenity existingAmenity = amenityRepository.findById(amenityId).orElseThrow(() -> new RuntimeException("Amenity not found"));
        existingAmenity.setAmenityName(amenity.getAmenityName());
        existingAmenity.setDescription(amenity.getDescription());
        existingAmenity.setAmenityStatus(amenity.getAmenityStatus());
        amenityRepository.save(existingAmenity);
        return AmenityDto.toDto(existingAmenity);
    }

    @Override
    public void deleteAmenity(int amenityId) {
        Amenity existingAmenity = amenityRepository.findById(amenityId).orElseThrow(() -> new RuntimeException("Amenity not found"));
        amenityRepository.delete(existingAmenity);
    }
}
