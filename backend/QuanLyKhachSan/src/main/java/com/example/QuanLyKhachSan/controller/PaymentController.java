package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.config.Config;
import com.example.QuanLyKhachSan.dto.BookingDto;
import com.example.QuanLyKhachSan.dto.PaymetnResDto;
import com.example.QuanLyKhachSan.dto.PaymentDto;
import com.example.QuanLyKhachSan.dto.TransactionResponseDto;
import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.Discount;
import com.example.QuanLyKhachSan.entity.Room;
import com.example.QuanLyKhachSan.enums.BookingStatus;
import com.example.QuanLyKhachSan.enums.PaymentMethod;
import com.example.QuanLyKhachSan.enums.PaymentStatus;
import com.example.QuanLyKhachSan.repository.CustomerRepository;
import com.example.QuanLyKhachSan.repository.DiscountRepository;
import com.example.QuanLyKhachSan.repository.RoomRepository;
import com.example.QuanLyKhachSan.service.BookingService;
import com.example.QuanLyKhachSan.entity.Booking;
import com.example.QuanLyKhachSan.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private BookingService bookingService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private PaymentService paymentService;


    // Map để lưu tạm thông tin booking theo transaction reference
    private static final Map<String, Map<String, Object>> pendingBookings = new HashMap<>();

    @GetMapping("/create")
    public ResponseEntity<?> createPayment(
            @RequestParam(value = "amount") long amount,
            @RequestParam(value = "content", required = false, defaultValue = "Payment for order") String content,
            @RequestParam(value = "customerId", required = false) String customerId,
            @RequestParam(value = "roomId", required = false) String roomId,
            @RequestParam(value = "checkInDate", required = false) String checkInDate,
            @RequestParam(value = "checkOutDate", required = false) String checkOutDate,
            @RequestParam(value = "numberOfGuests", required = false) Integer numberOfGuests,
            @RequestParam(value = "specialRequests", required = false) String specialRequests
    ) throws IOException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_TmnCode = Config.vnp_TmnCode;

        // Lưu thông tin booking tạm thời với transaction reference
        if (customerId != null && roomId != null) {
            Map<String, Object> bookingInfo = new HashMap<>();
            bookingInfo.put("customerId", customerId);
            bookingInfo.put("roomId", roomId);
            bookingInfo.put("checkInDate", checkInDate);
            bookingInfo.put("checkOutDate", checkOutDate);
            bookingInfo.put("numberOfGuests", numberOfGuests);
            bookingInfo.put("specialRequests", specialRequests);
            bookingInfo.put("amount", amount);
            bookingInfo.put("content", content); // Store content
            pendingBookings.put(vnp_TxnRef, bookingInfo);
            logger.info("Stored booking info for transaction: {}", vnp_TxnRef);
        }

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay yêu cầu amount nhân 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + content);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:3000/payment-callback");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        logger.info("Payment URL: {}", paymentUrl);

        PaymetnResDto paymentResDto = new PaymetnResDto();
        paymentResDto.setStatus("Ok");
        paymentResDto.setMessage("Success");
        paymentResDto.setURL(paymentUrl);
        return new ResponseEntity<>(paymentResDto, HttpStatus.OK);
    }

    @GetMapping("/payment-info")
    @Transactional
    public ResponseEntity<?> getPaymentInfo(
            @RequestParam(value = "vnp_Amount") String vnp_Amount,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(value = "vnp_ResponseCode", required = false) String vnp_ResponseCode,
            @RequestParam(value = "vnp_BankCode", required = false) String vnp_BankCode,
            @RequestParam(value = "vnp_PayDate", required = false) String vnp_PayDate
    ) {
        logger.info("Received params: vnp_Amount={}, vnp_TxnRef={}, vnp_ResponseCode={}, vnp_BankCode={}, vnp_PayDate={}",
                vnp_Amount, vnp_TxnRef, vnp_ResponseCode, vnp_BankCode, vnp_PayDate);

        try {
            if (vnp_ResponseCode == null || !vnp_ResponseCode.equals("00")) {
                logger.error("Payment failed with response code: {}", vnp_ResponseCode);
                return new ResponseEntity<>("Payment failed", HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> bookingInfo = pendingBookings.get(vnp_TxnRef);
            if (bookingInfo == null) {
                logger.error("No booking info found for transaction: {}", vnp_TxnRef);
                return new ResponseEntity<>("No booking info found", HttpStatus.BAD_REQUEST);
            }

            // Prepare response
            TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
            transactionResponseDto.setStatus("Success");
            transactionResponseDto.setMessage("Transaction completed successfully");
            transactionResponseDto.setTransactionId(vnp_TxnRef);
            transactionResponseDto.setTransactionDate(vnp_PayDate);
            transactionResponseDto.setAmount(vnp_Amount);
            logger.info("Transaction Info: {}", transactionResponseDto);
            return new ResponseEntity<>(transactionResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in getPaymentInfo: ", e);
            throw new RuntimeException("Error processing payment info: " + e.getMessage(), e);
        }
    }

    // Sửa method handlePaymentCallback trong PaymentController.java

    @PostMapping("/callback")
    @Transactional
    public ResponseEntity<?> handlePaymentCallback(@RequestBody Map<String, Object> paymentData) {
        try {
            String vnp_TxnRef = (String) paymentData.get("vnp_TxnRef");
            String vnp_ResponseCode = (String) paymentData.get("vnp_ResponseCode");
            String vnp_Amount = (String) paymentData.get("vnp_Amount");
            String vnp_PayDate = (String) paymentData.get("vnp_PayDate");

            logger.info("Payment callback received: txnRef={}, responseCode={}", vnp_TxnRef, vnp_ResponseCode);

            if (!"00".equals(vnp_ResponseCode)) {
                logger.error("Payment failed with response code: {}", vnp_ResponseCode);
                pendingBookings.remove(vnp_TxnRef);

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "failed");
                errorResponse.put("message", "Payment failed with code: " + vnp_ResponseCode);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> bookingInfo = pendingBookings.get(vnp_TxnRef);
            if (bookingInfo == null) {
                logger.error("No booking info found for transaction: {}", vnp_TxnRef);

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "failed");
                errorResponse.put("message", "No booking info found for transaction: " + vnp_TxnRef);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Create Booking and Payment
            Map<String, String> result = createBookingFromPayment(bookingInfo, vnp_TxnRef, vnp_Amount, vnp_PayDate);
            String bookingId = result.get("bookingId");
            String paymentId = result.get("paymentId");

            // Prepare success response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Booking and payment created successfully");
            response.put("bookingId", bookingId);
            response.put("paymentId", paymentId);
            response.put("transactionRef", vnp_TxnRef);
            response.put("amount", vnp_Amount);

            logger.info("Payment callback processed successfully: bookingId={}, paymentId={}", bookingId, paymentId);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error in payment callback: ", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "failed");
            errorResponse.put("message", "Error processing callback: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public Map<String, String> createBookingFromPayment(Map<String, Object> bookingInfo, String transactionRef, String vnp_Amount, String vnp_PayDate) {
        try {
            // Create Booking
            Booking booking = new Booking();

            // Set customer
            String customerId = (String) bookingInfo.get("customerId");
            logger.info("Fetching customer with ID: {}", customerId);
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            booking.setCustomer(customer);
            logger.info("Customer found: {}", customer.getCustomerId());

            // Set room
            Object roomIdObj = bookingInfo.get("roomId");
            List<Room> rooms = new ArrayList<>();
            if (roomIdObj instanceof List) {
                List<?> roomIds = (List<?>) roomIdObj;
                logger.info("Processing multiple rooms: {}", roomIds);
                for (Object roomId : roomIds) {
                    String id = (String) roomId;
                    Room room = roomRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Room not found: " + id));
                    rooms.add(room);
                }
            } else {
                String roomId = (String) roomIdObj;
                logger.info("Processing single room: {}", roomId);
                Room room = roomRepository.findById(roomId)
                        .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
                rooms.add(room);
            }
            booking.setRooms(rooms);
            logger.info("Rooms set: {}", rooms.stream().map(Room::getRoomId).collect(Collectors.toList()));

            // Parse dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String checkInDate = (String) bookingInfo.get("checkInDate");
            String checkOutDate = (String) bookingInfo.get("checkOutDate");
            logger.info("Parsing check-in date: {}, check-out date: {}", checkInDate, checkOutDate);
            booking.setCheckInDate(LocalDate.parse(checkInDate, formatter));
            booking.setCheckOutDate(LocalDate.parse(checkOutDate, formatter));

            // Set number of guests
            booking.setNumberOfGuests((Integer) bookingInfo.get("numberOfGuests"));
            logger.info("Number of guests: {}", booking.getNumberOfGuests());

            // Set special requests
            booking.setSpecialRequests((String) bookingInfo.get("specialRequests"));
            logger.info("Special requests: {}", booking.getSpecialRequests());

            // Set price
            Long totalPrice = ((Number) bookingInfo.get("amount")).longValue();
            booking.setTotalPrice(totalPrice);
            booking.setPriceBeforeDiscount(totalPrice);
            logger.info("Total price: {}", totalPrice);

            // Set discount if present
            Object discountIdObj = bookingInfo.get("discountId");
            if (discountIdObj != null) {
                String discountId = (String) discountIdObj;
                logger.info("Fetching discount with ID: {}", discountId);
                Discount discount = discountRepository.findById(discountId)
                        .orElseThrow(() -> new RuntimeException("Discount not found: " + discountId));
                booking.setDiscount(discount);
                logger.info("Discount set: {}", discount.getDiscountCode());
            }

            // Calculate total stays
            long totalStays = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
            booking.setTotalStays((int) totalStays);
            logger.info("Total stays: {}", totalStays);

            // Create BookingDto
            BookingDto bookingDto = new BookingDto();
            bookingDto.setCustomerId(customer.getCustomerId());
            bookingDto.setRoomIds(rooms.stream().map(Room::getRoomId).collect(Collectors.toList()));
            bookingDto.setCheckInDate(booking.getCheckInDate());
            bookingDto.setCheckOutDate(booking.getCheckOutDate());
            bookingDto.setNumberOfGuests(booking.getNumberOfGuests());
            bookingDto.setSpecialRequests(booking.getSpecialRequests());
            bookingDto.setTotalPrice(booking.getTotalPrice());
            bookingDto.setPriceBeforeDiscount(booking.getPriceBeforeDiscount());
            bookingDto.setBookingStatus(BookingStatus.PAID);
            bookingDto.setTotalStays(booking.getTotalStays());

            if (booking.getDiscount() != null) {
                bookingDto.setDiscountCode(booking.getDiscount().getDiscountCode());
                bookingDto.setDiscountPercentage(booking.getDiscount().getPercentage());
            }

            // Create booking
            logger.info("Creating booking with DTO: {}", bookingDto);
            BookingDto createdBookingDto = bookingService.createBooking(bookingDto);
            Booking createdBooking = BookingDto.toBookingEntity(createdBookingDto);
            String bookingId = createdBooking.getBookingId();
            logger.info("Booking created with ID: {}", bookingId);

            // Create Payment
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setBookingId(bookingId);
            paymentDto.setAmount(Long.parseLong(vnp_Amount) / 100);
            paymentDto.setPaymentMethod(PaymentMethod.VNPAY);
            paymentDto.setStatus(PaymentStatus.COMPLETED);
            paymentDto.setTransactionCode(transactionRef);
            paymentDto.setPaymentDate(LocalDateTime.now());
            paymentDto.setPaymentDetails((String) bookingInfo.get("content"));
            logger.info("Creating payment with DTO: {}", paymentDto);

            PaymentDto savedPayment = paymentService.createPayment(paymentDto);
            String paymentId = savedPayment.getPaymentId();
            logger.info("Payment created with ID: {}", paymentId);

            // Clear pending booking
            pendingBookings.remove(transactionRef);
            logger.info("Cleared pending booking for transactionRef: {}", transactionRef);

            // Return result
            Map<String, String> result = new HashMap<>();
            result.put("bookingId", bookingId);
            result.put("paymentId", paymentId);
            return result;

        } catch (Exception e) {
            logger.error("Error creating booking and payment for transactionRef: {}", transactionRef, e);
            throw new RuntimeException("Error creating booking and payment: " + e.getMessage(), e);
        }
    }
}