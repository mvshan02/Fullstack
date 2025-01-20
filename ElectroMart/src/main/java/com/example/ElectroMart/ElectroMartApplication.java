package com.example.ElectroMart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.ElectroMart.Repository")
public class ElectroMartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectroMartApplication.class, args);
		
	}


}
