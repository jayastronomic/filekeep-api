package com.example.filekeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FileKeepApplication {
	private static ApplicationContext context;
	public static void main(String[] args) {
		context = SpringApplication.run(FileKeepApplication.class, args);
	}


    public static ApplicationContext getContext() {
        return context;
    }
}
