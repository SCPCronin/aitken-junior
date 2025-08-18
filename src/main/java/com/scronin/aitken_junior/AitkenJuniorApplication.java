package com.scronin.aitken_junior;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://postgres:5432/postgres", "postgres", "mysecretpassword")) {
			// Connection successful
			System.out.println("Connected to the database!");
			return "Connected!";
		} catch (SQLException e) {
			// Handle any errors
			System.err.println("Connection failed: " + e.getMessage());
			return "Not connected!";
		}
	}
}
