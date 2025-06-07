package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.dto.UserDto;
import com.example.QuanLyKhachSan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Custom query methods can be added here

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByGoogleId(String googleId);

}