package com.example.QuanLyKhachSan.dto;

import com.example.QuanLyKhachSan.entity.ImageHotel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Getter
@Setter
public class ImageHotelDto {
    private String imageHotelId; // ID của ảnh khách sạn
    private String imageData; // Nội dung ảnh dưới dạng base64
    private MultipartFile imageFile; // File ảnh được upload từ client

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/uploads/images/hotel/";

    public static ImageHotelDto fromEntity(ImageHotel imageHotel) throws IOException {
        try
        {
            if (imageHotel == null) {
                throw new IllegalArgumentException("ImageHotel entity cannot be null");
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to convert ImageHotel to ImageHotelDto: " + e.getMessage());
        }
        ImageHotelDto imageHotelDto = new ImageHotelDto();
        imageHotelDto.setImageHotelId(imageHotel.getImageHotelId()); // Giả sử ImageHotel có getImageHotelId

        // Đọc file ảnh từ thư mục và chuyển thành base64
        String filePath = IMAGE_DIRECTORY + imageHotel.getImageHotelUrl();
        byte[] imageBytes = Files.readAllBytes(Paths.get(filePath));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        imageHotelDto.setImageData("data:image/jpeg;base64," + base64Image); // Thêm tiền tố cho frontend

        return imageHotelDto;

    }
}
