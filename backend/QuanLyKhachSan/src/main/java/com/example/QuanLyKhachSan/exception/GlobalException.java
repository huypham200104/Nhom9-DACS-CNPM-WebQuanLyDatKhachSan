package com.example.QuanLyKhachSan.exception;

import com.example.QuanLyKhachSan.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    // Xử lý lỗi khi validation không thành công
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();

        // Lấy thông tin lỗi từ các trường trong DTO
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            validationErrors.put(fieldName, message);
        });

        // Trả về lỗi với title và message
        return new ResponseEntity<>(ApiResponse.error("Bad Request", validationErrors.toString()), HttpStatus.BAD_REQUEST);
    }

    // Xử lý các exception khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(ApiResponse.error("Internal Server Error", "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
