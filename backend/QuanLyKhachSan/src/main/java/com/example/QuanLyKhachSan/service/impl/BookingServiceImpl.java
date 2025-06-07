package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.BookingDto;
import com.example.QuanLyKhachSan.dto.PaymentDto;
import com.example.QuanLyKhachSan.entity.*;
import com.example.QuanLyKhachSan.enums.BookingStatus;
import com.example.QuanLyKhachSan.enums.PaymentMethod;
import com.example.QuanLyKhachSan.enums.PaymentStatus;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.repository.*;
import com.example.QuanLyKhachSan.service.BookingService;
import com.example.QuanLyKhachSan.service.DiscountService;
import com.example.QuanLyKhachSan.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DiscountService discountService;

    @Override
    public BookingDto createBooking(BookingDto booking) {
        // Kiểm tra ngày tháng
        if (booking.getCheckInDate() == null || booking.getCheckOutDate() == null) {
            throw new CustomExceptions.InvalidInputException("Check-in and check-out dates are required");
        }

        if (booking.getCheckInDate().isAfter(booking.getCheckOutDate())) {
            throw new CustomExceptions.InvalidInputException("Check-in date cannot be after check-out date");
        }

        // Kiểm tra phòng
        if (booking.getRoomIds() == null || booking.getRoomIds().isEmpty()) {
            throw new CustomExceptions.InvalidInputException("At least one room must be selected");
        }

        for (String roomId : booking.getRoomIds()) {
            if (!roomRepository.existsById(roomId)) {
                throw new CustomExceptions.InvalidInputException("Room with ID " + roomId + " does not exist");
            }

            Date checkIn = Date.from(booking.getCheckInDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date checkOut = Date.from(booking.getCheckOutDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (!isRoomAvailable(roomId, checkIn, checkOut)) {
                throw new CustomExceptions.InvalidInputException("Room " + roomId + " is not available for the selected dates");
            }
        }
        if(booking.getDiscountCode() != null){
            Discount discount = discountRepository.findByDiscountCode(booking.getDiscountCode())
                    .orElseThrow(() -> new CustomExceptions.InvalidInputException("Discount code does not exist"));
            discount.setUsedCount(discount.getUsedCount() + 1);
        }

        // Kiểm tra xem booking có bị trùng không
        List<Booking> duplicates = bookingRepository.findPotentialDuplicates(
                booking.getCustomerId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomIds()
        );

        for (Booking existing : duplicates) {
            List<String> existingRoomIds = existing.getRooms().stream()
                    .map(Room::getRoomId)
                    .sorted()
                    .collect(Collectors.toList());

            List<String> requestedRoomIds = new ArrayList<>(booking.getRoomIds());
            Collections.sort(requestedRoomIds);

            if (existingRoomIds.equals(requestedRoomIds)) {
                log.warn("Booking already exists for customer ID: {}, dates: {} - {}, rooms: {}",
                        booking.getCustomerId(), booking.getCheckInDate(), booking.getCheckOutDate(), requestedRoomIds);
                return BookingDto.toBookingDto(existing);
            }
        }

        // Tính giá và áp dụng khuyến mãi
        int totalStays = calculateDaysBetween(
                Date.from(booking.getCheckInDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(booking.getCheckOutDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );

        // Tính tổng giá phòng
        long totalPrice = 0;
        List<Room> rooms = booking.getRoomIds().stream()
                .map(roomId -> roomRepository.findById(roomId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (Room room : rooms) {
            totalPrice += room.getPrice() * totalStays;
        }
        // Commented out: booking.setPriceBeforeDiscount(totalPrice);
        // Reason: Removing storage of pre-discount price to focus on final total price

        // Áp dụng khuyến mãi
        Discount discountEntity = null;
        if (booking.getDiscountCode() != null && !booking.getDiscountCode().trim().isEmpty()) {
            String hotelId = rooms.get(0).getHotel().getHotelId(); // All rooms belong to the same hotel
            discountEntity = discountService.validateDiscount(booking.getDiscountCode(), totalPrice, hotelId);

            // Áp dụng giảm giá theo phần trăm
            long discountAmount = totalPrice * discountEntity.getPercentage() / 100;
            totalPrice = totalPrice - discountAmount;
            // Commented out: booking.setDiscountPercentage(discountEntity.getPercentage());
            // Reason: Discount percentage is not needed in BookingDto; only final price matters
            // New code: Store discount percentage in the entity for reference if needed
            booking.setDiscountPercentage(discountEntity.getPercentage());

            // Tăng usedCount của discount
            discountEntity.setUsedCount(discountEntity.getUsedCount() != null ?
                    discountEntity.getUsedCount() + 1 : 1);
            discountRepository.save(discountEntity);
        }

        // Set thông tin và lưu
        booking.setTotalPrice(totalPrice); // Use the final (discounted) price
        booking.setTotalStays(totalStays);
        booking.setCreatedAt(LocalDate.now());
        booking.setUpdatedAt(LocalDate.now());

        if (booking.getBookingStatus() == null) {
            booking.setBookingStatus(BookingStatus.PENDING);
        }

        Booking bookingEntity = BookingDto.toBookingEntity(booking);

        Customer customer = customerRepository.findById(booking.getCustomerId()).orElse(null);
        bookingEntity.setCustomer(customer);

        bookingEntity.setRooms(rooms);

        if (discountEntity != null) {
            bookingEntity.setDiscount(discountEntity);
        }

        Booking savedBooking = bookingRepository.save(bookingEntity);
        log.info("Booking created successfully with ID: {}", savedBooking.getBookingId());
        return BookingDto.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto updateBooking(String bookingId, BookingDto bookingDTO) {
        log.info("Updating booking with ID: {}", bookingId);

        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Booking ID cannot be null or empty");
        }

        if (bookingDTO == null) {
            throw new CustomExceptions.InvalidInputException("Booking data cannot be null");
        }

        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Booking not found with ID: " + bookingId));

        // Update fields if provided
        if (bookingDTO.getCheckInDate() != null) {
            existingBooking.setCheckInDate(bookingDTO.getCheckInDate());
        }

        if (bookingDTO.getCheckOutDate() != null) {
            existingBooking.setCheckOutDate(bookingDTO.getCheckOutDate());
        }

        if (bookingDTO.getSpecialRequests() != null) {
            existingBooking.setSpecialRequests(bookingDTO.getSpecialRequests());
        }

        if (bookingDTO.getNumberOfGuests() != null) {
            existingBooking.setNumberOfGuests(bookingDTO.getNumberOfGuests());
        }

        // Recalculate total stays and price if dates changed
        if (bookingDTO.getCheckInDate() != null || bookingDTO.getCheckOutDate() != null) {
            LocalDate checkIn = bookingDTO.getCheckInDate() != null ?
                    bookingDTO.getCheckInDate() : existingBooking.getCheckInDate();
            LocalDate checkOut = bookingDTO.getCheckOutDate() != null ?
                    bookingDTO.getCheckOutDate() : existingBooking.getCheckOutDate();

            int totalStays = calculateDaysBetween(
                    Date.from(checkIn.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(checkOut.atStartOfDay(ZoneId.systemDefault()).toInstant())
            );

            // Recalculate price
            long totalPrice = 0;
            List<Room> rooms = existingBooking.getRooms();
            for (Room room : rooms) {
                totalPrice += room.getPrice() * totalStays;
            }
            // Commented out: existingBooking.setPriceBeforeDiscount(totalPrice);
            // Reason: Removing storage of pre-discount price to focus on final total price

            // Reapply discount if exists
            if (existingBooking.getDiscount() != null) {
                String discountCode = existingBooking.getDiscount().getDiscountCode();
                String hotelId = rooms.get(0).getHotel().getHotelId(); // All rooms belong to the same hotel
                Discount discountEntity = discountService.validateDiscount(discountCode, totalPrice, hotelId);

                long discountAmount = totalPrice * discountEntity.getPercentage() / 100;
                totalPrice = totalPrice - discountAmount;
                // New code: Update discount percentage in the entity for reference if needed
            }
            existingBooking.setTotalPrice(totalPrice); // Use the final (discounted) price
            existingBooking.setTotalStays(totalStays);
        }

        existingBooking.setUpdatedAt(LocalDate.now());
        existingBooking.setBookingStatus(
                bookingDTO.getBookingStatus() != null ? bookingDTO.getBookingStatus() : existingBooking.getBookingStatus()
        );
        bookingRepository.save(existingBooking);
        return BookingDto.toBookingDto(existingBooking);
    }

    @Override
    public Optional<BookingDto> getBookingById(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Booking ID cannot be null or empty");
        }

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.map(BookingDto::toBookingDto);
    }

    @Override
    public Page<BookingDto> getAllBookings(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new CustomExceptions.InvalidInputException("Page number and size must be valid");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);
        return bookingPage.map(BookingDto::toBookingDto);
    }



    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Customer ID cannot be null or empty");
        }

        List<Booking> bookings = bookingRepository.findByCustomer_CustomerId(customerId);
        return bookings.stream()
                .map(BookingDto::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByRoomId(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Room ID cannot be null or empty");
        }

        List<Booking> bookings = bookingRepository.findByRooms_RoomId(roomId);
        return bookings.stream()
                .map(BookingDto::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRoomAvailable(String roomId, Date checkInDate, Date checkOutDate) {
        log.info("Checking room availability for roomId: {}, checkInDate: {}, checkOutDate: {}", roomId, checkInDate, checkOutDate);
        if (roomId == null || roomId.trim().isEmpty() || checkInDate == null || checkOutDate == null) {
            log.error("Invalid input: roomId, checkInDate, or checkOutDate is null or empty");
            return false;
        }
        if (checkInDate.after(checkOutDate)) {
            log.error("Invalid date range: checkInDate is after checkOutDate");
            return false;
        }
        try {
            LocalDate checkIn = checkInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = checkOutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            log.info("Querying overlapping bookings for roomId: {}, checkIn: {}, checkOut: {}", roomId, checkIn, checkOut);
            List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                    roomId, checkIn, checkOut, Arrays.asList(BookingStatus.CANCELLED, BookingStatus.REFUNDED)
            );
            log.info("Found {} overlapping bookings", overlappingBookings.size());
            return overlappingBookings.isEmpty();
        } catch (Exception e) {
            log.error("Error checking room availability: {}", e.getMessage(), e);
            throw new CustomExceptions.InvalidInputException("Error checking room availability: " + e.getMessage());
        }
    }

    @Override
    @Transactional()
    public long calculateTotalPrice(String roomId, Date checkInDate, Date checkOutDate, String discountId) {
        if (roomId == null || checkInDate == null || checkOutDate == null) {
            return 0;
        }

        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return 0;
        }

        LocalDate checkIn = checkInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate checkOut = checkOutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);

        if (nights <= 0) {
            return 0;
        }

        long basePrice = room.getPrice() * nights;

        if (discountId != null && !discountId.trim().isEmpty()) {
            Optional<Discount> discount = discountRepository.findById(discountId);
            if (discount.isPresent()) {
                Discount discountEntity = discount.get();
                String hotelId = room.getHotel().getHotelId();
                discountService.validateDiscount(discountEntity.getDiscountCode(), basePrice, hotelId);

                long discountAmount = basePrice * discountEntity.getPercentage() / 100;
                basePrice -= discountAmount;
            }
        }

        return Math.max(0, basePrice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByStatus(String bookingStatus) {
        if (bookingStatus == null || bookingStatus.trim().isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Booking status cannot be null or empty");
        }

        try {
            BookingStatus status = BookingStatus.valueOf(bookingStatus.toUpperCase());
            List<Booking> bookings = bookingRepository.findByBookingStatus(status);
            return bookings.stream()
                    .map(BookingDto::toBookingDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new CustomExceptions.InvalidInputException("Invalid booking status: " + bookingStatus);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> getBookingsByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new CustomExceptions.InvalidInputException("Start date and end date cannot be null");
        }

        if (startDate.after(endDate)) {
            throw new CustomExceptions.InvalidInputException("Start date cannot be after end date");
        }

        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Booking> bookingPage = bookingRepository.findByCheckInDateBetween(start, end, pageable);

        return bookingPage.map(BookingDto::toBookingDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> getBookingsByHotelId(String hotelId, int page, int size) {
        if (hotelId == null || hotelId.trim().isEmpty()) {
            throw new CustomExceptions.InvalidInputException("Hotel ID cannot be null or empty");
        }

        if (page < 0 || size <= 0) {
            throw new CustomExceptions.InvalidInputException("Page number and size must be valid");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookingPage = bookingRepository.findBookingsByHotelId(hotelId, pageable);
        return bookingPage.map(BookingDto::toBookingDto);
    }

    int calculateDaysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và ngày kết thúc không được để trống");
        }
        long diffInMillies = endDate.getTime() - startDate.getTime();
        return (int) (diffInMillies / (1000 * 60 * 60 * 24));
    }
}