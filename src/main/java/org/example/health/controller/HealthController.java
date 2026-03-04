package org.example.health.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Health check controller for the AitkenJunior application.
 *
 * Provides a lightweight {@code GET /api/health} endpoint that can be called by:
 * - Docker Compose health checks (before marking the container as "healthy")
 * - Frontend applications to display system status
 * - Monitoring tools / uptime checkers
 *
 * Note: Spring Boot Actuator also provides a more detailed health endpoint at
 * {@code /actuator/health}. This controller provides a simpler, application-level
 * health summary that is safe to expose publicly without Actuator security concerns.
 */
@Slf4j
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * The application name, injected from {@code spring.application.name} in application.properties.
     * This confirms that configuration loading is working correctly.
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * The application version, injected from {@code app.version} in application.properties.
     */
    @Value("${app.version}")
    private String appVersion;

    /**
     * Returns a simple health status response.
     *
     * This endpoint always returns 200 OK if the application is running.
     * It includes the application name, version, and a server timestamp so
     * callers can verify that the backend clock is functioning as expected.
     *
     * @return {@link ApiResponse} wrapping a health status map
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        log.debug("Health check endpoint called");

        // Build the response payload as an ordered map for readable JSON output
        Map<String, Object> healthData = new LinkedHashMap<>();
        healthData.put("status", "UP");
        healthData.put("application", applicationName);
        healthData.put("version", appVersion);
        healthData.put("timestamp", LocalDateTime.now().toString());

        return ApiResponse.success("Application is healthy", healthData);
    }
}

