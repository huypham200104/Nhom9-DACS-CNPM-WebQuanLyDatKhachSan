package com.example.QuanLyKhachSan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mật khẩu mới của bạn");
        message.setText("Chào bạn,\n\n" +
                "Mật khẩu mới của bạn là: " + newPassword + "\n\n" +
                "Vui lòng đăng nhập và thay đổi mật khẩu nếu cần.\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ hỗ trợ");
        message.setFrom("huyphamforedu@gmail.com"); // Email của bạn
        mailSender.send(message);
    }
}