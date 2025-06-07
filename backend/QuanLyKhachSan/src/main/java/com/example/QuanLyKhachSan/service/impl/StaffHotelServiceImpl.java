package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.StaffHotelDto;
import com.example.QuanLyKhachSan.dto.StaffHotelWithHotelNameDto;
import com.example.QuanLyKhachSan.dto.StaffUserDto;
import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.StaffHotel;
import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.enums.PositionHotel;
import com.example.QuanLyKhachSan.enums.Role;
import com.example.QuanLyKhachSan.enums.UserStatus;
import com.example.QuanLyKhachSan.repository.HotelRepository;
import com.example.QuanLyKhachSan.repository.StaffHotelRepository;
import com.example.QuanLyKhachSan.repository.UserRepository;
import com.example.QuanLyKhachSan.service.StaffHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StaffHotelServiceImpl implements StaffHotelService {

    @Autowired
    private StaffHotelRepository staffHotelRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public StaffHotelDto addStaff(StaffUserDto staffHotel) {
        if (userRepository.existsByEmail(staffHotel.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (staffHotel.getRole() == null || !staffHotel.getRole().equalsIgnoreCase("staff")) {
            throw new IllegalArgumentException("Invalid role. Only 'staff' role is allowed.");
        }

        if (staffHotel.getPosition() == null ||
                (!staffHotel.getPosition().equalsIgnoreCase("MANAGER") &&
                        !staffHotel.getPosition().equalsIgnoreCase("RECEPTIONIST"))) {
            throw new IllegalArgumentException("Invalid position. Only 'MANAGER' or 'RECEPTIONIST' positions are allowed.");
        }

        User userEntity = new User();
        userEntity.setEmail(staffHotel.getEmail());
        userEntity.setPassword(User.encodePassword(staffHotel.getPassword()));
        userEntity.setRole(Role.valueOf(staffHotel.getRole().toUpperCase()));
        userEntity.setCreatedAt(LocalDate.now());
        userEntity.setUserStatus(UserStatus.ACTIVE);
        userEntity.setGoogleId(staffHotel.getGoogleId());

        userEntity = userRepository.save(userEntity);

        StaffHotel staffHotelEntity = new StaffHotel();
        staffHotelEntity.setUser(userEntity);
        staffHotelEntity.setHotel(staffHotel.getHotel());
        staffHotelEntity.setPosition(PositionHotel.valueOf(staffHotel.getPosition()));
        staffHotelEntity.setStartDate(staffHotel.getStartDate());
        staffHotelEntity.setEndDate(staffHotel.getEndDate());

        staffHotelEntity = staffHotelRepository.save(staffHotelEntity);

        return StaffHotelDto.toStaffHotelDto(staffHotelEntity);
    }

    @Override
    public StaffHotelDto updateStaff(String staffHotelId, StaffUserDto staffDto) {
        StaffHotel staffHotel = staffHotelRepository.findById(staffHotelId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Hotel hotel = hotelRepository.findById(staffDto.getHotel().getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        User user = staffHotel.getUser();
        // Cập nhật thông tin User

        if (staffDto.getEmail() != null) user.setEmail(staffDto.getEmail());
        if (staffDto.getPassword() != null) user.setPassword(User.encodePassword(staffDto.getPassword()));
        if (staffDto.getGoogleId() != null) user.setGoogleId(staffDto.getGoogleId());
        if (staffDto.getRole() != null) user.setRole(Role.valueOf(Role.STAFF.toString()));

        userRepository.save(user);


        // Cập nhật thông tin StaffHotel
        staffHotel.setHotel(hotel);
        staffHotel.setUser(user);
        if (staffDto.getPosition() != null) staffHotel.setPosition(PositionHotel.valueOf(staffDto.getPosition()));
        if (staffDto.getStartDate() != null) staffHotel.setStartDate(staffDto.getStartDate());
        if (staffDto.getEndDate() != null) staffHotel.setEndDate(staffDto.getEndDate());

        staffHotel = staffHotelRepository.save(staffHotel);
        // Trả về StaffHotelDto
        return StaffHotelDto.toStaffHotelDto(staffHotel);

    }

    @Override
    public StaffHotelDto getStaffById(String staffId) {
        StaffHotel staffHotel = staffHotelRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return StaffHotelDto.toStaffHotelDto(staffHotel);
    }

    @Override
    @Transactional
    public List<StaffHotelWithHotelNameDto> getAllStaffHotels(int page, int size) {
        List<StaffHotel> staffHotels = staffHotelRepository.findAll();
        return StaffHotelWithHotelNameDto.toStaffHotelWithHotelNameDtoList(staffHotels);
    }

    @Override
    public void deleteStaff(String staffId) {
        StaffHotel staffHotel = staffHotelRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));



        staffHotelRepository.delete(staffHotel);
        // Optional: xóa luôn User nếu cần
        userRepository.delete(staffHotel.getUser());
    }

    @Override
    public String getHotelIdByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StaffHotel staffHotel = staffHotelRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Staff hotel not found"));
        // Kiem tra hotel id co null khong
        if (staffHotel.getHotel() == null) {
            throw new RuntimeException("Hotel not found");
        }
        return staffHotel.getHotel().getHotelId();
    }
}
