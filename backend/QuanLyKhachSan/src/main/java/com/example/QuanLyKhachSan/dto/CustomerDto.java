package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String customerId;
    private String customerName;
    private String phone;
    private String userId; // Chỉ lấy ID của User thay vì toàn bộ đối tượng
    public static Customer toCustomerEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setCustomerId(customerDto.getCustomerId());
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setPhone(customerDto.getPhone());
        User user = new User();
        user.setUserId(customerDto.getUserId());
        customer.setUser(user);
        return customer;
    }
    public static CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getPhone(),
                customer.getUser() != null ? customer.getUser().getUserId() : null
        );
    }
}