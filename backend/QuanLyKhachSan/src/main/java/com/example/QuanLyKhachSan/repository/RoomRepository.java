package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByHotel(Hotel hotel);
    @Query("SELECT r FROM Room r")
    Page<Room> findAllRooms(Pageable pageable);

    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.imageRooms LEFT JOIN FETCH r.hotel LEFT JOIN FETCH r.roomType WHERE r.roomId IN :ids")
    List<Room> findByIdsWithFetch(@Param("ids") List<String> ids);

    @Query("SELECT r FROM Room r WHERE r.hotel = :hotel AND r.roomType = :roomType")
    List<Room> findByHotelAndRoomType(Hotel hotel, String roomType);

    @Query("SELECT DISTINCT r FROM Room r " +
            "JOIN FETCH r.hotel h " +
            "LEFT JOIN FETCH r.imageRooms " +
            "WHERE LOWER(h.city) LIKE LOWER(:city)")
    List<Room> findRoomsWithImagesByCity(@Param("city") String city);
}
