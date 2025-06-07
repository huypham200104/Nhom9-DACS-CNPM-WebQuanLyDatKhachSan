package com.example.QuanLyKhachSan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

@SpringBootApplication
@CrossOrigin(origins = "*")
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class QuanLyKhachSanApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuanLyKhachSanApplication.class, args);
	}

}
