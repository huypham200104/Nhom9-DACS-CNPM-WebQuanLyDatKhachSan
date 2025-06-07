package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.dto.CustomerDto;
import com.example.QuanLyKhachSan.dto.CustomerUserDto;
import com.example.QuanLyKhachSan.entity.Booking;
import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.enums.Role;
import com.example.QuanLyKhachSan.enums.UserStatus;
import com.example.QuanLyKhachSan.repository.BookingRepository;
import com.example.QuanLyKhachSan.repository.CustomerRepository;
import com.example.QuanLyKhachSan.repository.UserRepository;
import com.example.QuanLyKhachSan.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerSerivceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Override
    @Transactional
    public CustomerDto addCustomer(CustomerUserDto customer) {

        // 2. Check if email already exists
        if (userRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }

        // 3. Create and save User entity
        User userEntity = new User();
        userEntity.setEmail(customer.getEmail());
        userEntity.setPassword(User.encodePassword(customer.getPassword()));
        userEntity.setRole(Role.CUSTOMER);
        userEntity.setCreatedAt(LocalDate.now());
        userEntity.setUserStatus(UserStatus.ACTIVE);
        userEntity.setGoogleId(customer.getGoogleId());

        // Save User to generate user_id
        userEntity = userRepository.save(userEntity);

        // 4. Create and save Customer entity
        Customer customerEntity = new Customer();
        // Không gán customerId thủ công, để Hibernate tự tạo
        customerEntity.setCustomerName(customer.getCustomerName());
        customerEntity.setPhone(customer.getPhone());
        customerEntity.setUser(userEntity);

        // Save Customer
        customerEntity = customerRepository.save(customerEntity);

        // 5. Return CustomerDto
        return CustomerDto.toCustomerDto(customerEntity);
    }



    @Override
    @Transactional
    public CustomerDto updateCustomer(String userId, CustomerUserDto customer) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId));

        // Kiểm tra xem người dùng có phải là khách hàng không
        if (user.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("Người dùng không phải là khách hàng");
        }
        // Tìm khách hàng theo userId
        Customer existingCustomer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với userId: " + userId));
        // Cập nhật thông tin khách hàng
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setPhone(customer.getPhone());

        // Cập nhật thông tin người dùng
        user.setPassword(User.encodePassword(customer.getPassword()));

        // Lưu khách hàng đã cập nhật
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return new CustomerDto(
                updatedCustomer.getCustomerId(),
                updatedCustomer.getCustomerName(),
                updatedCustomer.getPhone(),
                updatedCustomer.getUser().getUserId()
        );
    }

    @Override
    @Transactional
    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + customerId));
        customerRepository.delete(customer);
    }

    @Override
    public CustomerDto getCustomerById(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + customerId));
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getPhone(),
                customer.getUser().getUserId()
        );
    }

    @Override
    public List<CustomerDto> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        if (customerPage.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng nào");
        }
        return customerPage.getContent().stream()
                .map(customer -> new CustomerDto(
                        customer.getCustomerId(),
                        customer.getCustomerName(),
                        customer.getPhone(),
                        customer.getUser().getUserId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> searchCustomers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findByCustomerNameContainingIgnoreCaseOrPhoneContaining(keyword, keyword, pageable);
        if (customerPage.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng nào với từ khóa: " + keyword);
        }
        return customerPage.getContent().stream()
                .map(customer -> new CustomerDto(
                        customer.getCustomerId(),
                        customer.getCustomerName(),
                        customer.getPhone(),
                        customer.getUser().getUserId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> getCustomersByBookingId(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đặt phòng với ID: " + bookingId));
        Customer customer = booking.getCustomer();
        if (customer == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng liên quan đến đặt phòng này");
        }
        return List.of(new CustomerDto(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getPhone(),
                customer.getUser().getUserId()
        ));
    }

    @Override
    public List<CustomerDto> getCustomersByCheckInDate(LocalDate checkInDate) {
        List<Booking> bookings = bookingRepository.findByCheckInDate(checkInDate);
        if (bookings.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng nào với ngày nhận phòng: " + checkInDate);
        }
        return bookings.stream()
                .map(booking -> {
                    Customer customer = booking.getCustomer();
                    return new CustomerDto(
                            customer.getCustomerId(),
                            customer.getCustomerName(),
                            customer.getPhone(),
                            customer.getUser().getUserId()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> getCustomersByCheckOutDate(LocalDate checkOutDate) {
        List<Booking> bookings = bookingRepository.findByCheckOutDate(checkOutDate);
        if (bookings.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng nào với ngày trả phòng: " + checkOutDate);
        }
        return bookings.stream()
                .map(booking -> {
                    Customer customer = booking.getCustomer();
                    return new CustomerDto(
                            customer.getCustomerId(),
                            customer.getCustomerName(),
                            customer.getPhone(),
                            customer.getUser().getUserId()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerUserDto getCustomerByUserId(String userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId));

        // Kiểm tra xem người dùng có phải là khách hàng không
        if (user.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("Người dùng không phải là khách hàng");
        }

        // Tìm khách hàng theo userId
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với userId: " + userId));

        return new CustomerUserDto(
                customer.getCustomerId(),
                user.getUserId(),
                customer.getCustomerName(),
                customer.getPhone(),
                user.getRole().name(),
                user.getEmail(),
                null, // Mật khẩu không trả về
                user.getCreatedAt(),
                user.getGoogleId(),
                user.getUserStatus().name()
        );
    }

}
