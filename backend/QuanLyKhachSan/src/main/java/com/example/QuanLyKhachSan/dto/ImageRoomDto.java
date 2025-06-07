package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.ImageRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Getter
@Setter
public class ImageRoomDto {
    private int imageRoomId; // ID hình ảnh (nếu cập nhật)
    private String imageData; // Nội dung ảnh dưới dạng base64
    private MultipartFile imageFile; // File ảnh được upload từ client

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/uploads/images/room/";

    public static ImageRoomDto fromEntity(ImageRoom imageRoom) throws IOException {
        ImageRoomDto imageRoomDto = new ImageRoomDto();
        imageRoomDto.setImageRoomId(imageRoom.getImageRoomId()); // Giả sử ImageRoom có getImageRoomId

        // Đọc file ảnh từ thư mục và chuyển thành base64
        String filePath = IMAGE_DIRECTORY + imageRoom.getImageRoomUrl();
        byte[] imageBytes = Files.readAllBytes(Paths.get(filePath));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        imageRoomDto.setImageData("data:image/jpeg;base64," + base64Image); // Thêm tiền tố cho frontend

        return imageRoomDto;
    }
}