package com.example.QuanLyKhachSan.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Ẩn tất cả các trường null
public class ApiResponse<T> {
    private String title;
    private T data;
    private String message;

    public ApiResponse(String title) {
        this.title = title;
    }
    // Constructor for success
    public ApiResponse(String title, T data) {
        this.title = title;
        this.data = data;
    }

    // Constructor for error
    public ApiResponse(String title, String message) {
        this.title = title;
        this.message = message;
    }

    // Static factory method for success
    public static <T> ApiResponse<T> success(String title, T data) {
        return new ApiResponse<>(title, data);
    }

    // Static factory method for error
    public static <T> ApiResponse<T> error(String title, String message) {
        return new ApiResponse<>(title, message);
    }
}
