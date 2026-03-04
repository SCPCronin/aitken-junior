package org.example.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for the {@link org.example.health.controller.HealthController}.
 *
 * Uses {@code @SpringBootTest} to load the full application context and
 * {@code MockMvc} to make HTTP requests without starting a real server.
 *
 * {@code @ActiveProfiles("test")} activates application-test.properties, which:
 * - Uses H2 in-memory database (no Docker required)
 * - Disables the Discord bot
 * - Disables Flyway (schema managed by Hibernate create-drop in tests)
 *
 * Adding new tests:
 * Follow the Arrange / Act / Assert pattern used below.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Health Controller Integration Tests")
class HealthControllerTest {

    /**
     * MockMvc simulates HTTP requests to the application without a running server.
     * Spring Boot auto-configures it via @AutoConfigureMockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies that the health endpoint returns 200 OK with the expected JSON shape.
     * This is the most important test — it confirms the entire Spring context wires up
     * correctly (application, controller, configuration).
     */
    @Test
    @DisplayName("GET /api/health returns 200 OK with status UP")
    void healthEndpoint_returns200WithStatusUp() throws Exception {
        mockMvc.perform(get("/api/health")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Verify the ApiResponse wrapper fields
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Application is healthy"))
                // Verify the data payload fields
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.application").value("AitkenJunior"))
                .andExpect(jsonPath("$.data.version").exists())
                .andExpect(jsonPath("$.data.timestamp").exists());
    }

    /**
     * Verifies that the response does not contain an 'errors' field on success.
     * The {@code @JsonInclude(NON_NULL)} on ApiResponse should exclude null fields.
     */
    @Test
    @DisplayName("GET /api/health response does not contain null error fields")
    void healthEndpoint_doesNotExposeNullFields() throws Exception {
        mockMvc.perform(get("/api/health")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").doesNotExist());
    }
}

