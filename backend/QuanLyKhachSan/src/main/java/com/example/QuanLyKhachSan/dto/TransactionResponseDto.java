package com.example.QuanLyKhachSan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponseDto {
    private String status;
    private String message;
    private String transactionId;
    private String transactionDate;
    private String amount;
}
