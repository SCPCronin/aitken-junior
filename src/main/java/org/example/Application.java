package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application entry point for AitkenJunior.
 *
 * This is a personal projects backend infrastructure designed to be easily extensible
 * for various use cases like note management, quizzes, and more.
 *
 * Architecture Overview:
 * - REST API layer (Controllers) - Handles HTTP requests
 * - Business Logic layer (Services) - Contains domain logic and orchestration
 * - Data Access layer (Repositories) - Manages database interactions
 * - Domain Models (Entities) - Represent core domain objects
 * - Configuration layer - Manages application setup
 *
 * Each feature/module should follow this layered structure for consistency and maintainability.
 *
 * @author Sean Cronin
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.example"})
public class Application {

    /**
     * Main method to bootstrap the Spring Boot application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}