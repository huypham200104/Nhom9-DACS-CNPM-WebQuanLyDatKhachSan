package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Booking;
import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.Discount;
import com.example.QuanLyKhachSan.entity.Room;
import com.example.QuanLyKhachSan.enums.BookingStatus;
import com.example.QuanLyKhachSan.enums.PaymentMethod;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private String bookingId;
    private String customerId;
    private List<String> roomIds;
    // Cho phep null
    private String discountCode;
    private int discountPercentage;
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;

    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;
    private Long totalPrice;
    private Long priceBeforeDiscount;
    private PaymentMethod paymentMethod;
    private BookingStatus bookingStatus;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String specialRequests;
    @Min(value = 1, message = "There must be at least 1 guest")
    @Max(value = 100, message = "Too many guests")
    private Integer numberOfGuests;
    private int totalStays;


    private String hotelName;
    private String roomType;
    private List<CustomerDto> customers;

    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDto dto = new BookingDto();
        dto.setBookingId(booking.getBookingId());

        Customer customer = booking.getCustomer();
        if (customer != null) {
            dto.setCustomerId(customer.getCustomerId());
        }

        List<Room> rooms = booking.getRooms();
        if (rooms != null && !rooms.isEmpty()) {
            dto.setRoomIds(rooms.stream().map(Room::getRoomId).collect(Collectors.toList()));

            if (rooms.get(0).getHotel() != null) {
                dto.setHotelName(rooms.get(0).getHotel().getHotelName());
            }

            if (rooms.get(0).getRoomType() != null) {
                dto.setRoomType(rooms.get(0).getRoomType().getRoomTypeName());
            }
        } else {
            dto.setRoomIds(new ArrayList<>());
        }

        if (booking.getDiscount() != null) {
            dto.setDiscountCode(booking.getDiscount().getDiscountCode());
            dto.setDiscountPercentage(booking.getDiscount().getPercentage());
        } else {
            dto.setDiscountCode(null);
            dto.setDiscountPercentage(0);
        }

        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setPriceBeforeDiscount(booking.getPriceBeforeDiscount());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
        dto.setSpecialRequests(booking.getSpecialRequests());
        dto.setNumberOfGuests(booking.getNumberOfGuests());
        dto.setTotalStays(booking.getTotalStays());

        return dto;
    }

    public static Booking toBookingEntity(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setBookingId(bookingDto.getBookingId());

        if (bookingDto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setCustomerId(bookingDto.getCustomerId());
            booking.setCustomer(customer);
        }

        if (bookingDto.getRoomIds() != null) {
            List<Room> rooms = bookingDto.getRoomIds().stream()
                    .map(roomId -> {
                        Room room = new Room();
                        room.setRoomId(roomId);
                        return room;
                    }).collect(Collectors.toList());
            booking.setRooms(rooms);
        }

        if (bookingDto.getDiscountCode() != null) {
            Discount discount = new Discount();
            discount.setDiscountCode(bookingDto.getDiscountCode());
            discount.setPercentage(bookingDto.getDiscountPercentage());
            booking.setDiscount(discount);
        }

        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setTotalPrice(bookingDto.getTotalPrice()); // đã thêm
        booking.setPriceBeforeDiscount(bookingDto.getPriceBeforeDiscount()); // đã thêm
        booking.setBookingStatus(bookingDto.getBookingStatus());
        booking.setCreatedAt(bookingDto.getCreatedAt());
        booking.setUpdatedAt(bookingDto.getUpdatedAt());
        booking.setSpecialRequests(bookingDto.getSpecialRequests());
        booking.setNumberOfGuests(bookingDto.getNumberOfGuests());
        booking.setTotalStays(bookingDto.getTotalStays());

        return booking;
    }
}
