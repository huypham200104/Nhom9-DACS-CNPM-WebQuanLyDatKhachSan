package com.example.QuanLyKhachSan.controller;

import com.example.QuanLyKhachSan.response.ApiResponse;
import com.example.QuanLyKhachSan.dto.CustomerDto;
import com.example.QuanLyKhachSan.dto.CustomerUserDto;
import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerDto>> addCustomer(@Valid @RequestBody CustomerUserDto customerDto) {
        try {
            CustomerDto newCustomer = customerService.addCustomer(customerDto);
            return ResponseEntity.ok(ApiResponse.success("Đăng ký khách hàng thành công", newCustomer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Bad Request", e.getMessage()));
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Bad Request", "Dữ liệu không hợp lệ"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal Server Error", e.getMessage()));
        }
    }


    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@PathVariable String userId,@RequestBody CustomerUserDto customer) {
        try {
            CustomerDto updatedCustomer = customerService.updateCustomer(userId, customer);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật khách hàng thành công", updatedCustomer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable String customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return ResponseEntity.ok(new ApiResponse<>("Xóa khách hàng thành công", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerById(@PathVariable String customerId) {
        try {
            CustomerDto customer = customerService.getCustomerById(customerId);
            return ResponseEntity.ok(new ApiResponse<>("Lấy thông tin khách hàng thành công", customer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<CustomerDto> customers = customerService.getAllCustomers(page, size);
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách khách hàng thành công", customers));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> searchCustomers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<CustomerDto> customers = customerService.searchCustomers(keyword, page, size);
            return ResponseEntity.ok(new ApiResponse<>("Tìm kiếm khách hàng thành công", customers));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getCustomersByBookingId(@PathVariable String bookingId) {
        try {
            List<CustomerDto> customers = customerService.getCustomersByBookingId(bookingId);
            return ResponseEntity.ok(new ApiResponse<>("Lấy thông tin khách hàng theo đặt phòng thành công", customers));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }

    @GetMapping("/check-in")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getCustomersByCheckInDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate) {
        try {
            List<CustomerDto> customers = customerService.getCustomersByCheckInDate(checkInDate);
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách khách hàng theo ngày nhận phòng thành công", customers));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }

    @GetMapping("/check-out")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getCustomersByCheckOutDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        try {
            List<CustomerDto> customers = customerService.getCustomersByCheckOutDate(checkOutDate);
            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách khách hàng theo ngày trả phòng thành công", customers));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CustomerUserDto>> getCustomerByUserId(@PathVariable String userId) {
        try {
            CustomerUserDto customer = customerService.getCustomerByUserId(userId);
            return ResponseEntity.ok(new ApiResponse<>("Get customer by user id is succed", customer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi không xác định: " + e.getMessage(), null));
        }
    }

}