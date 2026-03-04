package org.example.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler for the entire application.
 *
 * {@code @RestControllerAdvice} is a Spring mechanism that intercepts exceptions
 * thrown by any {@code @RestController} in the application, before they reach
 * the client. This ensures ALL error responses follow the same {@link ApiResponse}
 * structure — consistent, predictable, and easy to handle on the frontend.
 *
 * Adding a new handler:
 * 1. Create a method annotated with {@code @ExceptionHandler(YourException.class)}
 * 2. Return a {@code ResponseEntity<ApiResponse<?>>} with the appropriate HTTP status
 * 3. Log the error at the appropriate level (WARN for expected errors, ERROR for unexpected)
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} — thrown when an entity is not found.
     * Returns HTTP 404 Not Found with a descriptive message.
     *
     * @param ex The exception containing the "not found" message
     * @return 404 response with ApiResponse error body
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Handles {@link BusinessLogicException} — thrown when a domain rule is violated.
     * Returns HTTP 422 Unprocessable Entity with a user-friendly message.
     *
     * @param ex The exception describing the business rule violation
     * @return 422 response with ApiResponse error body
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessLogic(BusinessLogicException ex) {
        log.warn("Business logic violation: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Handles {@link MethodArgumentNotValidException} — thrown when a request body
     * fails Bean Validation (e.g., {@code @NotBlank}, {@code @Size} annotations on DTOs).
     *
     * Collects all field-level validation errors into a structured list so the client
     * knows exactly which fields were invalid and why.
     *
     * @param ex The validation exception containing all field errors
     * @return 400 response with a list of validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Map each field error to an ApiResponse.ErrorDetail for a structured response
        List<ApiResponse.ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ApiResponse.ErrorDetail(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getCode()
                ))
                .toList();

        log.warn("Validation failed with {} error(s)", errors.size());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.validationError(errors));
    }

    /**
     * Catch-all handler for any unexpected exception.
     *
     * This is the last line of defence — if an exception isn't caught by a more
     * specific handler above, it lands here. Returns HTTP 500 with a generic message
     * to avoid leaking internal stack traces to the client.
     *
     * The full exception is logged at ERROR level for investigation.
     *
     * @param ex Any uncaught exception
     * @return 500 response with a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred. Please try again later."));
    }
}

