package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    Customer findByCustomerId(String customerId);


    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> searchCustomers(@Param("keyword") String keyword);

    Page<Customer> findByCustomerNameContainingIgnoreCaseOrPhoneContaining(String keyword, String keyword1, Pageable pageable);

    @Query("SELECT c FROM Customer c JOIN c.user u WHERE u.userId = :userId")
    Optional<Customer> findByUserId(@Param("userId") String userId);
}
