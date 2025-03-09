package com.kt.karry_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KarryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KarryBackendApplication.class, args);
	}

}
