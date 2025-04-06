package com.scronin.aitken_junior;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AitkenJuniorApplication {
	public static void main(String[] args) {
		SpringApplication.run(AitkenJuniorApplication.class, args);
	}
}

@RestController
@RequestMapping("/api")
class HelloController {
	@GetMapping("/hello")
	public String hello() {
		return "Hello, Spring Boot!";
	}
}
