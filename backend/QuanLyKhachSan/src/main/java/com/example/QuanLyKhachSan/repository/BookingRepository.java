package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Booking;
import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.Room;
import com.example.QuanLyKhachSan.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    /**
     * Tìm các đặt phòng theo customer ID
     */
    List<Booking> findByCustomer_CustomerId(String customerId);

    /**
     * Tìm các đặt phòng theo customer ID với phân trang
     */
    Page<Booking> findByCustomer_CustomerId(String customerId, Pageable pageable);

    /**
     * Tìm các đặt phòng theo room ID
     */
    @Query("SELECT b FROM Booking b JOIN b.rooms r WHERE r.roomId = :roomId")
    List<Booking> findByRooms_RoomId(@Param("roomId") String roomId);

    /**
     * Tìm các đặt phòng theo room ID với phân trang
     */
    @Query("SELECT b FROM Booking b JOIN b.rooms r WHERE r.roomId = :roomId")
    Page<Booking> findByRooms_RoomId(@Param("roomId") String roomId, Pageable pageable);

    /**
     * Tìm các đặt phòng theo trạng thái
     */
    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    /**
     * Tìm các đặt phòng theo trạng thái với phân trang
     */
    Page<Booking> findByBookingStatus(BookingStatus bookingStatus, Pageable pageable);

    /**
     * Tìm các đặt phòng trong khoảng thời gian check-in
     */
    Page<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Tìm các đặt phòng trong khoảng thời gian check-in (không phân trang)
     */
    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Tìm các đặt phòng trong khoảng thời gian check-out
     */
    List<Booking> findByCheckOutDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Tìm các đặt phòng có overlap về thời gian với room cụ thể (loại trừ trạng thái cancelled)
     * Dùng để kiểm tra room availability
     */
    @Query("SELECT b FROM Booking b JOIN b.rooms r " +
            "WHERE r.roomId = :roomId " +
            "AND b.bookingStatus NOT IN :excludedStatuses " +
            "AND (b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)")
    List<Booking> findOverlappingBookings(String roomId, LocalDate checkInDate, LocalDate checkOutDate, List<BookingStatus> excludedStatuses);

    /**
     * Kiểm tra xem có booking nào overlap với thời gian và phòng cụ thể không
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b JOIN b.rooms r WHERE r.roomId = :roomId " +
            "AND b.bookingStatus NOT IN (:excludeStatuses) " +
            "AND ((b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate))")
    boolean existsOverlappingBookings(@Param("roomId") String roomId,
                                      @Param("checkInDate") LocalDate checkInDate,
                                      @Param("checkOutDate") LocalDate checkOutDate,
                                      @Param("excludeStatuses") List<BookingStatus> excludeStatuses);

    /**
     * Tìm các đặt phòng theo customer và trạng thái
     */
    List<Booking> findByCustomer_CustomerIdAndBookingStatus(String customerId, BookingStatus bookingStatus);

    /**
     * Tìm các đặt phòng theo discount code
     */
    List<Booking> findByDiscount_DiscountCode(String discountCode);

    /**
     * Tìm các đặt phòng được tạo trong khoảng thời gian
     */
    List<Booking> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Tìm các đặt phòng được cập nhật trong khoảng thời gian
     */
    List<Booking> findByUpdatedAtBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Đếm số lượng đặt phòng theo trạng thái
     */
    long countByBookingStatus(BookingStatus bookingStatus);

    /**
     * Đếm số lượng đặt phòng của customer
     */
    long countByCustomer_CustomerId(String customerId);

    /**
     * Tìm các đặt phòng có special requests
     */
    List<Booking> findBySpecialRequestsIsNotNull();

    /**
     * Tìm các đặt phòng theo số lượng khách
     */
    List<Booking> findByNumberOfGuests(int numberOfGuests);

    /**
     * Tìm các đặt phòng có số lượng khách lớn hơn hoặc bằng
     */
    List<Booking> findByNumberOfGuestsGreaterThanEqual(int numberOfGuests);

    /**
     * Tìm các đặt phòng có tổng giá trong khoảng
     */
    List<Booking> findByTotalPriceBetween(Long minPrice, Long maxPrice);

    /**
     * Tìm các đặt phòng active (chưa bị hủy, chưa hoàn tiền, chưa no-show) trong khoảng thời gian
     */
    @Query("SELECT b FROM Booking b WHERE b.bookingStatus IN (:activeStatuses) " +
            "AND ((b.checkInDate <= :endDate AND b.checkOutDate >= :startDate))")
    List<Booking> findActiveBookingsInDateRange(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("activeStatuses") List<BookingStatus> activeStatuses);

    /**
     * Tìm các đặt phòng của customer trong khoảng thời gian
     */
    @Query("SELECT b FROM Booking b WHERE b.customer.customerId = :customerId " +
            "AND ((b.checkInDate <= :endDate AND b.checkOutDate >= :startDate))")
    List<Booking> findCustomerBookingsInDateRange(@Param("customerId") String customerId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    /**
     * Tìm các đặt phòng sắp tới (check-in trong vài ngày tới) với trạng thái đã xác nhận hoặc đã thanh toán
     */
    @Query("SELECT b FROM Booking b WHERE b.checkInDate BETWEEN :today AND :futureDate " +
            "AND b.bookingStatus IN (:statuses) ORDER BY b.checkInDate ASC")
    List<Booking> findUpcomingBookings(@Param("today") LocalDate today,
                                       @Param("futureDate") LocalDate futureDate,
                                       @Param("statuses") List<BookingStatus> statuses);

    /**
     * Tìm các đặt phòng quá hạn (check-out đã qua nhưng chưa checked out)
     */
    @Query("SELECT b FROM Booking b WHERE b.checkOutDate < :today " +
            "AND b.bookingStatus NOT IN (:excludeStatuses)")
    List<Booking> findOverdueBookings(@Param("today") LocalDate today,
                                      @Param("excludeStatuses") List<BookingStatus> excludeStatuses);

    /**
     * Thống kê doanh thu theo tháng (chỉ tính booking đã thanh toán)
     */
    @Query("SELECT MONTH(b.createdAt) as month, YEAR(b.createdAt) as year, SUM(b.totalPrice) as revenue " +
            "FROM Booking b WHERE b.bookingStatus IN (:paidStatuses) " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY YEAR(b.createdAt), MONTH(b.createdAt) " +
            "ORDER BY YEAR(b.createdAt), MONTH(b.createdAt)")
    List<Object[]> getMonthlyRevenue(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     @Param("paidStatuses") List<BookingStatus> paidStatuses);

    /**
     * Tìm top khách hàng theo số lượng booking (loại trừ cancelled, no_show, refunded)
     */
    @Query("SELECT b.customer.customerId, COUNT(b) as bookingCount " +
            "FROM Booking b WHERE b.bookingStatus NOT IN (:excludeStatuses) " +
            "GROUP BY b.customer.customerId " +
            "ORDER BY bookingCount DESC")
    List<Object[]> findTopCustomersByBookingCount(@Param("excludeStatuses") List<BookingStatus> excludeStatuses,
                                                  Pageable pageable);

    /**
     * Tìm các đặt phòng theo nhiều room IDs
     */
    @Query("SELECT DISTINCT b FROM Booking b JOIN b.rooms r WHERE r.roomId IN :roomIds")
    List<Booking> findByMultipleRoomIds(@Param("roomIds") List<String> roomIds);

    /**
     * Xóa soft các booking quá cũ (chuyển thành cancelled)
     */
    @Query("UPDATE Booking b SET b.bookingStatus = :newStatus, b.updatedAt = :updateDate " +
            "WHERE b.createdAt < :cutoffDate AND b.bookingStatus = :oldStatus")
    int softDeleteOldBookings(@Param("cutoffDate") LocalDate cutoffDate,
                              @Param("oldStatus") BookingStatus oldStatus,
                              @Param("newStatus") BookingStatus newStatus,
                              @Param("updateDate") LocalDate updateDate);


    List<Booking> findByCheckInDate(LocalDate checkInDate);

    List<Booking> findByCheckOutDate(LocalDate checkOutDate);

    @Query("SELECT b FROM Booking b WHERE b.customer.customerId = :customerId " +
            "AND b.checkInDate = :checkInDate AND b.checkOutDate = :checkOutDate")
    List<Booking> findByCustomer_CustomerIdAndCheckInDateAndCheckOutDate(
            @Param("customerId") String customerId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);


    @Query("SELECT b FROM Booking b JOIN b.rooms r WHERE b.customer.customerId = :customerId AND b.checkInDate = :checkInDate AND b.checkOutDate = :checkOutDate AND r.roomId IN :roomIds")
    List<Booking> findPotentialDuplicates(@Param("customerId") String customerId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate,
                                          @Param("roomIds") List<String> roomIds);


    @Query("SELECT b FROM Booking b JOIN b.rooms r WHERE r.hotel.hotelId = :hotelId")
    Page<Booking> findBookingsByHotelId(@Param("hotelId") String hotelId, Pageable pageable);

}