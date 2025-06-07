package com.example.QuanLyKhachSan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "customer_id", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private String customerId;

    @Column(name = "customer_name", length = 255, nullable = true)
    private String customerName;

    @Column(name = "phone", length = 11, nullable = true)
    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id",
            columnDefinition = "CHAR(36)", nullable = true)
    private User user;

}