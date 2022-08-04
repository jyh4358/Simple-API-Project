package com.backendcodingtest.codingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CodingtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingtestApplication.class, args);
	}

}
