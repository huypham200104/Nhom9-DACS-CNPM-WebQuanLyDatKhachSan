package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.*;
import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.ImageHotel;
import com.example.QuanLyKhachSan.entity.StaffHotel;
import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.enums.ApprovalStatus;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.repository.HotelRepository;
import com.example.QuanLyKhachSan.repository.ImageHotelRepository;
import com.example.QuanLyKhachSan.repository.StaffHotelRepository;
import com.example.QuanLyKhachSan.repository.UserRepository;
import com.example.QuanLyKhachSan.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ImageHotelRepository imageHotelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StaffHotelRepository staffHotelRepository;
    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/uploads/images/hotel/";

    @Override
    public HotelDto getHotelById(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId));
        List<ImageHotel> imageHotels = imageHotelRepository.findByHotel(hotel);

        return HotelDto.fromEntity(hotel, imageHotels);
    }
    @Override
    public Page<HotelDto> getAllHotels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Hotel> hotelPage = hotelRepository.findAll(pageable);
        List<HotelDto> hotelDtos = new ArrayList<>();
        System.out.println(hotelPage.getContent());
        for(Hotel hotel : hotelPage.getContent()) {
            System.out.println("Hotel ID: " + hotel.getHotelId());
            List<ImageHotel> imageHotels = imageHotelRepository.findByHotel(hotel);
            System.out.println("Image Hotels: " + imageHotels);
            hotelDtos.add(HotelDto.fromEntity(hotel, imageHotels));

        }
        return new PageImpl<>(hotelDtos, pageable, hotelPage.getTotalElements());
    }


    @Override
    public HotelDto createHotel(Hotel hotel, List<MultipartFile> imageFiles) {
        if (hotel.getHotelId() != null) {
            throw new CustomExceptions.InvalidInputException("Hotel ID must be null when creating a new hotel.");
        }
        if (hotel.getApprovalStatus() == null) {
            hotel.setApprovalStatus(ApprovalStatus.PENDING);
        }
        if (hotel.getCreatedAt() == null) {
            hotel.setCreatedAt(LocalDateTime.now());
        }
        if (hotel.getUpdatedAt() == null) {
            hotel.setUpdatedAt(LocalDateTime.now());
        }
        hotelRepository.save(hotel);
        System.out.println("Saved hotel: " + hotel.getHotelId());

        List<ImageHotel> imageHotels = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            System.out.println("Processing " + imageFiles.size() + " images");
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String fileName = saveHotelImage(file);
                    System.out.println("Saved image: " + fileName);
                    ImageHotel imageHotel = new ImageHotel();
                    imageHotel.setImageHotelUrl(fileName);
                    imageHotel.setHotel(hotel);
                    imageHotels.add(imageHotel);
                } else {
                    System.out.println("Empty file detected, skipping...");
                }
            }
            imageHotelRepository.saveAll(imageHotels);
            System.out.println("Saved " + imageHotels.size() + " images to database");
        } else {
            System.out.println("No images provided");
        }
        return HotelDto.fromEntity(hotel, imageHotels);
    }

    @Override
    public HotelDto updateHotel(String hotelId, Hotel hotel, List<MultipartFile> imageFiles) {
        Hotel existingHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId));

        // Cập nhật thông tin khách sạn
        existingHotel.setHotelName(hotel.getHotelName());
        existingHotel.setHotelRating(hotel.getHotelRating());
        existingHotel.setCity(hotel.getCity());
        existingHotel.setDistrict(hotel.getDistrict());
        existingHotel.setWard(hotel.getWard());
        existingHotel.setStreet(hotel.getStreet());
        existingHotel.setHouseNumber(hotel.getHouseNumber());
        existingHotel.setLatitude(hotel.getLatitude());
        existingHotel.setLongitude(hotel.getLongitude());
        existingHotel.setApprovalStatus(hotel.getApprovalStatus());
        existingHotel.setApprovedBy(hotel.getApprovedBy());
        existingHotel.setApprovedAt(hotel.getApprovedAt());
        // Cập nhật thời gian cập nhật
        existingHotel.setUpdatedAt(LocalDateTime.now());

        // Lưu ảnh vào thư mục và lấy danh sách tên ảnh
        List<String> savedImageNames = saveHotelImages(imageFiles);

        // Luu danh sach anh vao database
        List<ImageHotel> imageHotels = new ArrayList<>();
        if(imageFiles != null && !imageFiles.isEmpty()) {
            for(MultipartFile file : imageFiles) {
                String fileName = saveHotelImage(file);
                ImageHotel imageHotel = new ImageHotel();
                imageHotel.setImageHotelUrl(fileName);
                imageHotel.setHotel(existingHotel);
                imageHotels.add(imageHotel);
            }
            imageHotelRepository.saveAll(imageHotels);
        }

        hotelRepository.save(existingHotel);
        return HotelDto.fromEntity(existingHotel, imageHotels);
    }

    @Override
    public void deleteHotel(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId));

        // Xóa tất cả ảnh liên quan đến khách sạn
        List<ImageHotel> imageHotels = imageHotelRepository.findByHotel(hotel);
        for (ImageHotel imageHotel : imageHotels) {
            deleteHotelImage(imageHotel.getImageHotelUrl());
        }
        imageHotelRepository.deleteAll(imageHotels);

        // Xóa khách sạn
        hotelRepository.delete(hotel);
    }

    @Override
    public HotelDto approveHotel(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId));

        // Cập nhật trạng thái phê duyệt
        hotel.setApprovalStatus(ApprovalStatus.APPROVED);
        hotel.setUpdatedAt(LocalDateTime.now());
        hotelRepository.save(hotel);

        return HotelDto.fromEntity(hotel, imageHotelRepository.findByHotel(hotel));
    }

    @Override
    public HotelDto rejectHotel(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId));

        // Cập nhật trạng thái phê duyệt
        hotel.setApprovalStatus(ApprovalStatus.REJECTED);
        hotel.setUpdatedAt(LocalDateTime.now());
        hotelRepository.save(hotel);

        return HotelDto.fromEntity(hotel, imageHotelRepository.findByHotel(hotel));
    }

    @Override
    public HotelDto getHotelByName(String hotelName) {
        Hotel hotel = hotelRepository.findByHotelName(hotelName)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Hotel not found with name: " + hotelName));
        List<ImageHotel> imageHotels = imageHotelRepository.findByHotel(hotel);
        return HotelDto.fromEntity(hotel, imageHotels);
    }

    @Override
    public Page<HotelDto> getHotelByCity(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Hotel> spec = (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(city)) {
                return criteriaBuilder.equal(root.get("city"), city);
            }
            return criteriaBuilder.conjunction();
        };
        Page<Hotel> hotelPage = hotelRepository.findAll(spec, pageable);
        List<HotelDto> hotelDtos = hotelPage.getContent().stream()
                .map(hotel -> HotelDto.fromEntity(hotel, imageHotelRepository.findByHotel(hotel)))
                .collect(Collectors.toList());
        return new PageImpl<>(hotelDtos, pageable, hotelPage.getTotalElements());
    }

    @Override
    public Page<HotelDto> getHotelByDistrict(String district, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Hotel> spec = (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(district)) {
                return criteriaBuilder.equal(root.get("district"), district);
            }
            return criteriaBuilder.conjunction();
        };
        Page<Hotel> hotelPage = hotelRepository.findAll(spec, pageable);
        List<HotelDto> hotelDtos = hotelPage.getContent().stream()
                .map(hotel -> HotelDto.fromEntity(hotel, imageHotelRepository.findByHotel(hotel)))
                .collect(Collectors.toList());
        return new PageImpl<>(hotelDtos, pageable, hotelPage.getTotalElements());
    }

    @Override
    public Page<HotelDto> getHotelByWard(String ward, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Hotel> spec = (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(ward)) {
                return criteriaBuilder.equal(root.get("ward"), ward);
            }
            return criteriaBuilder.conjunction();
        };
        Page<Hotel> hotelPage = hotelRepository.findAll(spec, pageable);
        List<HotelDto> hotelDtos = hotelPage.getContent().stream()
                .map(hotel -> HotelDto.fromEntity(hotel, imageHotelRepository.findByHotel(hotel)))
                .collect(Collectors.toList());
        return new PageImpl<>(hotelDtos, pageable, hotelPage.getTotalElements());

    }

    @Override
    public Page<HotelDto> getHotelByStreet(String street, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Hotel> spec = (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(street)) {
                return criteriaBuilder.equal(root.get("street"), street);
            }
            return criteriaBuilder.conjunction();
        };
        Page<Hotel> hotelPage = hotelRepository.findAll(spec, pageable);
        List<HotelDto> hotelDtos = hotelPage.getContent().stream()
                .map(hotel -> HotelDto.fromEntity(hotel, imageHotelRepository.findByHotel(hotel)))
                .collect(Collectors.toList());
        return new PageImpl<>(hotelDtos, pageable, hotelPage.getTotalElements());
    }

    @Override
    public List<HotelDto> searchHotels(String city, String district, String ward, String street, String houseNumber) {
        Hotel hotel = hotelRepository.findByFullAddress(city, district, ward, street, houseNumber)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Hotel not found with address: " + city + ", " + district + ", " + ward + ", " + street + ", " + houseNumber));

        List<ImageHotel> imageHotels = imageHotelRepository.findByHotel(hotel);
        return imageHotels.stream()
                .map(imageHotel -> HotelDto.fromEntity(hotel, imageHotels))
                .collect(Collectors.toList());
    }

    @Override
    public HotelDto getHotelByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("User not found with id: " + userId));

        StaffHotel staffHotel = staffHotelRepository.findByUser(user)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("StaffHotel not found with user id: " + userId));
        Hotel hotel = hotelRepository.findById(staffHotel.getHotel().getHotelId())
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + staffHotel.getHotel().getHotelId()));
        List<ImageHotel> imageHotels = imageHotelRepository.findByHotel(hotel);
        return HotelDto.fromEntity(hotel, imageHotels);
    }

    @Override
    public List<String> getAllHotelNames() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(Hotel::getHotelName)
                .collect(Collectors.toList());
    }

    public String saveHotelImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty!");
        }

        File directory = new File(IMAGE_DIRECTORY);
        System.out.println("Saving to directory: " + IMAGE_DIRECTORY);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            System.out.println("Directory created: " + created);
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + IMAGE_DIRECTORY);
            }
        }
        if (!directory.canWrite()) {
            throw new RuntimeException("No write permission for directory: " + IMAGE_DIRECTORY);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(IMAGE_DIRECTORY, fileName);
        System.out.println("Saving file to: " + filePath.toString());

        try {
            Files.copy(file.getInputStream(), filePath);
            System.out.println("File saved successfully: " + fileName);
            return fileName;
        } catch (IOException e) {
            System.err.println("Failed to save image: " + e.getMessage());
            throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
        }
    }

    public List<String> saveHotelImages(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = saveHotelImage(file);
                    fileNames.add(fileName);
                }
            }
        }
        return fileNames;
    }

    public void deleteHotelImage(String imageName) {
        if (imageName != null && !imageName.isEmpty()) {
            Path filePath = Paths.get(IMAGE_DIRECTORY, imageName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image: " + e.getMessage());
            }
        }
    }

}
