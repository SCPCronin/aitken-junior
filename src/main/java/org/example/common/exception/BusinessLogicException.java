package org.example.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a business rule or domain constraint is violated.
 *
 * This should be used for application-level errors that are predictable and
 * understandable by the end user — for example, "A quiz can only be started
 * once", or "You cannot delete a note that is pinned".
 *
 * Annotated with {@code @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)} (422)
 * because the request was well-formed but could not be processed due to
 * business logic constraints.
 *
 * Usage:
 * <pre>
 *   if (quiz.isAlreadyStarted()) {
 *       throw new BusinessLogicException("Quiz has already been started");
 *   }
 * </pre>
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessLogicException extends RuntimeException {

    /**
     * Creates a business logic exception with a descriptive message explaining
     * which rule was violated.
     *
     * @param message A human-readable description of the business rule that was violated.
     */
    public BusinessLogicException(String message) {
        super(message);
    }

    /**
     * Creates a business logic exception with a message and the underlying cause.
     * Use this when wrapping another exception that triggered the violation.
     *
     * @param message A human-readable description of the business rule that was violated.
     * @param cause   The underlying exception that triggered this error.
     */
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}

