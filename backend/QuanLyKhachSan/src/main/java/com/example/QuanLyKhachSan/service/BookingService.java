package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.BookingDto;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    // 1. Tạo mới một đặt phòng
    BookingDto createBooking(BookingDto bookingDTO);

    // 2. Lấy thông tin đặt phòng theo booking_id
    Optional<BookingDto> getBookingById(String bookingId);

    // 3. Lấy danh sách tất cả đặt phòng
    Page<BookingDto> getAllBookings(int page, int size);

    // 4. Cập nhật thông tin đặt phòng
    BookingDto updateBooking(String bookingId, BookingDto bookingDTO);

    // 6. Lấy danh sách đặt phòng theo customer_id
    List<BookingDto> getBookingsByCustomerId(String customerId);

    // 7. Lấy danh sách đặt phòng theo room_id
    List<BookingDto> getBookingsByRoomId(String roomId);

    // 8. Kiểm tra tính khả dụng của phòng trong khoảng thời gian
    boolean isRoomAvailable(String roomId, Date checkInDate, Date checkOutDate);

    // 9. Tính toán tổng giá (bao gồm áp dụng discount nếu có)
    long calculateTotalPrice(String roomId, Date checkInDate, Date checkOutDate, String discountId);

    // 11. Lấy danh sách đặt phòng theo trạng thái đặt phòng
    List<BookingDto> getBookingsByStatus(String bookingStatus);


    // 13. Lấy danh sách đặt phòng trong khoảng thời gian
    Page<BookingDto> getBookingsByDateRange(Date startDate, Date endDate);
    // 14. Lay danh sach booking theo hotelId
    Page<BookingDto> getBookingsByHotelId(String hotelId, int page, int size);

}