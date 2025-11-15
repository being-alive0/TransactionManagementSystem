package com.example.transactionsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching; // 1. Import this

@SpringBootApplication
@EnableCaching // 2. Add this annotation
public class TransactionSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionSystemApplication.class, args);
	}

}
