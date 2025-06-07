package com.example.QuanLyKhachSan.exception;

public class CustomExceptions {

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message) {
            super(message);
        }
    }
    public static class ResourceAlreadyExistsException extends RuntimeException {
        public ResourceAlreadyExistsException(String message) {
            super(message);
        }
    }
    public static class TokenException extends RuntimeException {
        public TokenException(String message) {
            super(message);
        }
    }
    public static class EmptyImageUrlListException extends RuntimeException {
        public EmptyImageUrlListException(String message) {
            super(message);
        }
    }
}