package org.example.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource cannot be found in the system.
 *
 * Annotated with {@code @ResponseStatus(HttpStatus.NOT_FOUND)} so that Spring MVC
 * automatically returns a 404 HTTP status when this exception propagates out of a
 * controller. The {@link GlobalExceptionHandler} provides a more structured response
 * body using {@code ApiResponse.error(...)}.
 *
 * Usage:
 * <pre>
 *   Note note = noteRepository.findById(id)
 *       .orElseThrow(() -> new ResourceNotFoundException("Note", id));
 * </pre>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a generic "resource not found" exception with a plain message.
     *
     * @param message A human-readable description of what was not found.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a "resource not found" exception with a structured message
     * identifying both the resource type and the ID that was looked up.
     *
     * @param resourceName The name of the entity type (e.g., "Note", "Quiz")
     * @param id           The identifier that was not found
     */
    public ResourceNotFoundException(String resourceName, Object id) {
        super(String.format("%s not found with id: %s", resourceName, id));
    }
}

