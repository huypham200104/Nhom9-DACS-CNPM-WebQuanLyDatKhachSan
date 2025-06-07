package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.RoomDto;
import com.example.QuanLyKhachSan.entity.*;
import com.example.QuanLyKhachSan.repository.HotelRepository;
import com.example.QuanLyKhachSan.repository.ImageRoomRepository;
import com.example.QuanLyKhachSan.repository.RoomRepository;
import com.example.QuanLyKhachSan.repository.RoomTypeRepository;
import com.example.QuanLyKhachSan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {


    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ImageRoomRepository imageRoomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/uploads/images/room/";

    @Override
    public RoomDto getRoomById(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        List<ImageRoom> imageRooms = imageRoomRepository.findByRoom(room);
        return RoomDto.fromEntity(room, imageRooms);
    }

    @Override
    public Page<RoomDto> getAllRooms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> roomPage = roomRepository.findAll(pageable);
        List<RoomDto> roomDtos = new ArrayList<>();

        for (Room room : roomPage.getContent()) {
            List<ImageRoom> imageRooms = imageRoomRepository.findByRoom(room);
            roomDtos.add(RoomDto.fromEntity(room, imageRooms));
        }

        return new PageImpl<>(roomDtos, pageable, roomPage.getTotalElements());
    }

    @Override
    public RoomDto addRoom(RoomDto roomDTO, List<MultipartFile> files) {
        // Kiểm tra roomDTO không null
        if (roomDTO == null) {
            throw new IllegalArgumentException("RoomDTO cannot be null");
        }
        // Kiểm tra hotelId và roomTypeId không null
        if (roomDTO.getHotelId() == null) {
            throw new IllegalArgumentException("Hotel ID cannot be null or empty");
        }
        if (roomDTO.getRoomTypeId() == null || roomDTO.getRoomTypeId().isEmpty()) {
            throw new IllegalArgumentException("Room Type ID cannot be null or empty");
        }

        // Tìm hotel theo hotelId từ RoomDTO
        Hotel hotel = hotelRepository.findById(roomDTO.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        RoomType roomType = roomTypeRepository.findById(roomDTO.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        // Chuyển RoomDTO thành Room entity
        Room room = new Room();
        room.setDescription(roomDTO.getDescription());
        room.setMaxGuests(roomDTO.getMaxGuests());
        room.setPrice(roomDTO.getPrice());
        room.setRoomSize(roomDTO.getRoomSize());
        room.setRoomStatus(roomDTO.getRoomStatus());
        room.setBedType(roomDTO.getBedType());
        room.setRoomType(roomType);
        room.setHotel(hotel);
        room.setCreatedAt(LocalDate.now());
        room.setUpdatedAt(LocalDate.now());

        // Lưu phòng vào database
        room = roomRepository.save(room);

        // Lưu ảnh vào thư mục và lấy danh sách tên ảnh
        List<String> savedImageNames = saveRoomImages(files);

        // Lưu danh sách ảnh vào database
        List<ImageRoom> imageRooms = new ArrayList<>();
        for (String imageName : savedImageNames) {
            ImageRoom imageRoom = new ImageRoom();
            imageRoom.setRoom(room);
            imageRoom.setImageRoomUrl(imageName);
            imageRooms.add(imageRoom);
        }
        imageRoomRepository.saveAll(imageRooms);

        // Trả về RoomDto với danh sách ảnh
        return RoomDto.fromEntity(room, imageRooms);
    }

    @Override
    public RoomDto updateRoom(String roomId, RoomDto roomDTO, List<MultipartFile> files) {
        // Kiểm tra roomId và roomDTO
        if (roomId == null || roomId.isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
        if (roomDTO == null) {
            throw new IllegalArgumentException("RoomDTO cannot be null");
        }

        // Tìm phòng theo roomId
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Cập nhật thông tin phòng từ RoomDTO
        existingRoom.setDescription(roomDTO.getDescription());
        existingRoom.setMaxGuests(roomDTO.getMaxGuests());
        existingRoom.setPrice(roomDTO.getPrice());
        existingRoom.setRoomSize(roomDTO.getRoomSize());
        existingRoom.setRoomStatus(roomDTO.getRoomStatus());
        existingRoom.setBedType(roomDTO.getBedType());
        existingRoom.setUpdatedAt(LocalDate.now());

        // Cập nhật hotel nếu cần
        if (roomDTO.getHotelId() != null) {
            Hotel hotel = hotelRepository.findById(roomDTO.getHotelId())
                    .orElseThrow(() -> new RuntimeException("Hotel not found"));
            existingRoom.setHotel(hotel);
        }

        // Cập nhật roomType nếu cần
        if (roomDTO.getRoomTypeId() != null && !roomDTO.getRoomTypeId().isEmpty()) {
            RoomType roomType = roomTypeRepository.findById(roomDTO.getRoomTypeId())
                    .orElseThrow(() -> new RuntimeException("Room type not found"));
            existingRoom.setRoomType(roomType);
        }

        // Lưu ảnh mới vào thư mục và lấy danh sách tên ảnh
        List<String> savedImageNames = saveRoomImages(files);

        // Xóa các ảnh cũ
        List<ImageRoom> oldImageRooms = imageRoomRepository.findByRoom(existingRoom);
        for (ImageRoom oldImage : oldImageRooms) {
            deleteRoomImage(oldImage.getImageRoomUrl());
            imageRoomRepository.delete(oldImage);
        }

        // Lưu danh sách ảnh mới vào database
        List<ImageRoom> newImageRooms = new ArrayList<>();
        for (String imageName : savedImageNames) {
            ImageRoom imageRoom = new ImageRoom();
            imageRoom.setRoom(existingRoom);
            imageRoom.setImageRoomUrl(imageName);
            newImageRooms.add(imageRoom);
        }
        imageRoomRepository.saveAll(newImageRooms);

        // Lưu phòng đã cập nhật
        roomRepository.save(existingRoom);

        // Trả về RoomDto với danh sách ảnh
        return RoomDto.fromEntity(existingRoom, newImageRooms);
    }

    @Override
    public void deleteRoom(String roomId) {
        // Kiểm tra roomId
        if (roomId == null || roomId.isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }

        // Tìm phòng
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Xóa các ảnh liên quan
        List<ImageRoom> imageRooms = imageRoomRepository.findByRoom(room);
        for (ImageRoom imageRoom : imageRooms) {
            deleteRoomImage(imageRoom.getImageRoomUrl());
            imageRoomRepository.delete(imageRoom);
        }

        // Xóa phòng
        roomRepository.delete(room);
    }

    @Override
    public List<RoomDto> getRoomByHotelId(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        List<Room> rooms = roomRepository.findByHotel(hotel);
        if (rooms.isEmpty()) {
            throw new RuntimeException("No rooms found for this hotel");
        }

        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : rooms) {
            List<ImageRoom> imageRooms = imageRoomRepository.findByRoom(room);
            roomDtos.add(RoomDto.fromEntity(room, imageRooms));
        }
        return roomDtos;
    }

    @Override
    @Transactional
    public Page<RoomDto> getAvailableRooms(String city, LocalDate checkInDate, LocalDate checkOutDate, Integer adults, Integer children, int page, int size) {
        // Validate input parameters
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Check-in and check-out dates cannot be null");
        }
        if (adults == null || adults <= 0) {
            throw new IllegalArgumentException("Adults must be greater than zero");
        }
        if (children == null || children < 0) {
            throw new IllegalArgumentException("Children cannot be negative");
        }
        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        // Find hotels in the specified city
        List<Hotel> hotels = hotelRepository.findByCity(city);
        if (hotels.isEmpty()) {
            throw new RuntimeException("No hotels found in this city");
        }

        List<RoomDto> availableRooms = new ArrayList<>();
        for (Hotel hotel : hotels) {
            List<Room> rooms = roomRepository.findByHotel(hotel);
            for (Room room : rooms) {
                // Check room status - only consider available rooms
                if (!"AVAILABLE".equals(room.getRoomStatus())) {
                    continue;
                }

                // Check guest capacity: adults + children * 0.5
                double totalGuests = adults + (children * 0.5);
                if (room.getMaxGuests() < totalGuests) {
                    continue;
                }

                // Check room availability
                boolean isAvailable = true;
                List<Booking> bookings = room.getBookings();
                if (bookings != null) {
                    for (Booking booking : bookings) {
                        // Skip cancelled bookings
                        if ("CANCELLED".equals(booking.getBookingStatus())) {
                            continue;
                        }

                        // Check for date overlap
                        if (checkInDate.isBefore(booking.getCheckOutDate()) &&
                                checkOutDate.isAfter(booking.getCheckInDate())) {
                            isAvailable = false;
                            break;
                        }
                    }
                }

                if (isAvailable) {
                    List<ImageRoom> imageRooms = imageRoomRepository.findByRoom(room);
                    availableRooms.add(RoomDto.fromEntity(room, imageRooms));
                }
            }
        }

        // Paginate results
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), availableRooms.size());
        int end = Math.min(start + pageable.getPageSize(), availableRooms.size());
        List<RoomDto> paginatedRooms = availableRooms.subList(start, end);
        return new PageImpl<>(paginatedRooms, pageable, availableRooms.size());
    }


    public String saveRoomImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty!");
        }

        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + IMAGE_DIRECTORY);
            }
        }

        // Tạo tên file duy nhất để tránh trùng lặp
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(IMAGE_DIRECTORY, fileName);

        try {
            file.transferTo(filePath.toFile());
            return fileName; // Trả về tên file để lưu vào database
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    public List<String> saveRoomImages(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = saveRoomImage(file);
                    fileNames.add(fileName);
                }
            }
        }
        return fileNames;
    }

    public void deleteRoomImage(String imageName) {
        if (imageName != null && !imageName.isEmpty()) {
            Path filePath = Paths.get(IMAGE_DIRECTORY, imageName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image: " + e.getMessage());
            }
        }
    }
    @Override
    public List<LocalDate> getAvailableDatesForRoom(String roomId, LocalDate startDate, LocalDate endDate) {
        // Validate input dates
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range provided");
        }

        // Find the room by ID
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));

        // Get all bookings for the room
        List<Booking> bookings = room.getBookings();


        // Create a list of all dates in the requested range
        List<LocalDate> allDates = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            allDates.add(date);
        }

        // Filter out dates that fall within any booking's check-in and check-out range
        List<LocalDate> availableDates = allDates.stream()
                .filter(date -> bookings.stream().noneMatch(booking ->
                        !date.isBefore(booking.getCheckInDate()) &&
                                !date.isAfter(booking.getCheckOutDate().minusDays(1))
                ))
                .collect(Collectors.toList());

        return availableDates;
    }

    @Override
    public Page<RoomDto> getRoomsByHotelId(String hotelId, int page, int size) {
        // Validate hotelId
        if (hotelId == null || hotelId.isEmpty()) {
            throw new IllegalArgumentException("Hotel ID cannot be null or empty");
        }

        // Find hotel by ID
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        // Get rooms for the hotel
        List<Room> rooms = roomRepository.findByHotel(hotel);
        if (rooms.isEmpty()) {
            throw new RuntimeException("No rooms found for this hotel");
        }

        // Convert rooms to RoomDto
        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : rooms) {
            List<ImageRoom> imageRooms = imageRoomRepository.findByRoom(room);
            roomDtos.add(RoomDto.fromEntity(room, imageRooms));
        }

        // Paginate results
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), roomDtos.size());
        int end = Math.min(start + pageable.getPageSize(), roomDtos.size());
        List<RoomDto> paginatedRooms = roomDtos.subList(start, end);

        return new PageImpl<>(paginatedRooms, pageable, roomDtos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> getRoomByCity(String city, int page, int size) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }

        // Sử dụng JOIN để tránh N+1 problem
        List<Room> rooms = roomRepository.findRoomsWithImagesByCity(city);

        if (rooms.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(page, size), 0);
        }

        List<RoomDto> roomDtos = rooms.stream()
                .map(room -> RoomDto.fromEntity(room, room.getImageRooms()))
                .collect(Collectors.toList());

        // Paginate results
        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), roomDtos.size());
        int end = Math.min(start + pageable.getPageSize(), roomDtos.size());
        List<RoomDto> paginatedRooms = roomDtos.subList(start, end);

        return new PageImpl<>(paginatedRooms, pageable, roomDtos.size());
    }
}