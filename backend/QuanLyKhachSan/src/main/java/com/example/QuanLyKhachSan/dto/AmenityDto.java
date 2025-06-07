package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Amenity;
import com.example.QuanLyKhachSan.enums.AmenityStatus;

public class AmenityDto {
    private int amenityId;
    private String amenityName;
    private String description;
    private AmenityStatus amenityStatus;

    // Constructors
    public AmenityDto() {
    }

    public AmenityDto(int amenityId, String amenityName, String description) {
        this.amenityId = amenityId;
        this.amenityName = amenityName;
        this.description = description;
        this.amenityStatus = AmenityStatus.AVAILABLE; // Default status
    }

    // Getters and Setters
    public int getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(int amenityId) {
        this.amenityId = amenityId;
    }

    public String getAmenityName() {
        return amenityName;
    }

    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AmenityStatus getAmenityStatus() {
        return amenityStatus;
    }

    public void setAmenityStatus(AmenityStatus amenityStatus) {
        this.amenityStatus = amenityStatus;
    }

    // You might want to add toString(), equals(), and hashCode() methods as needed
    @Override
    public String toString() {
        return "AmenityDto{" +
                "amenityId=" + amenityId +
                ", amenityName='" + amenityName + '\'' +
                ", description='" + description + '\'' +
                ", amenityStatus=" + amenityStatus +
                '}';
    }
    public static AmenityDto toDto(Amenity amenity){
        AmenityDto amenityDto = new AmenityDto();
        amenityDto.setAmenityId(amenity.getAmenityId());
        amenityDto.setAmenityName(amenity.getAmenityName());
        amenityDto.setDescription(amenity.getDescription());
        amenityDto.setAmenityStatus(amenity.getAmenityStatus());
        return amenityDto;
    }
}