package com.example.filekeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FileKeepApplication {
	public static void main(String[] args) {
		SpringApplication.run(FileKeepApplication.class, args);
	}
}
