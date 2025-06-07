package com.example.QuanLyKhachSan.repository;

import com.example.QuanLyKhachSan.entity.ImageRoom;
import com.example.QuanLyKhachSan.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRoomRepository extends JpaRepository<ImageRoom, Integer> {
    List<ImageRoom> findByRoom(Room room);
    List<ImageRoom> findByRoomIn(List<Room> rooms);

}
