package com.example.productmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
public class ProductMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductMasterApplication.class, args);
	}

}
