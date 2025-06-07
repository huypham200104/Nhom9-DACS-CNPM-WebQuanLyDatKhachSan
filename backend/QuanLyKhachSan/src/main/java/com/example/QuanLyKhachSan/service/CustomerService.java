package com.example.QuanLyKhachSan.service;

import com.example.QuanLyKhachSan.dto.CustomerDto;
import com.example.QuanLyKhachSan.dto.CustomerUserDto;
import com.example.QuanLyKhachSan.entity.Customer;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CustomerService {
    public CustomerDto addCustomer(CustomerUserDto customer);
    public CustomerDto updateCustomer(String userId, CustomerUserDto customer);
    public void deleteCustomer(String customerId);
    public CustomerDto getCustomerById(String customerId);
    public List<CustomerDto> getAllCustomers(int page, int size); // Thêm phân trang
    public List<CustomerDto> searchCustomers(String keyword, int page, int size); // Tìm kiếm theo tên hoặc địa chỉ
    public List<CustomerDto> getCustomersByBookingId(String bookingId); // Lấy danh sách khách hàng theo mã đặt phòng
    public List<CustomerDto> getCustomersByCheckInDate(LocalDate checkInDate); // Lấy danh sách khách hàng theo ngày nhận phòng
    public List<CustomerDto> getCustomersByCheckOutDate(LocalDate checkOutDate); // Lấy danh sách khách hàng theo ngày trả phòng

    public CustomerUserDto getCustomerByUserId(String userId); // Lấy thông tin khách hàng theo userId
}
