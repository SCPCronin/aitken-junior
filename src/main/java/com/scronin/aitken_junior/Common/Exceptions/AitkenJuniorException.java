package com.scronin.aitken_junior.Common.Exceptions;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AitkenJuniorException extends Exception {

    private final int errorCode;

    // Constructor with message and error code
    public AitkenJuniorException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // Constructor with message, error code, and cause (stack trace)
    public AitkenJuniorException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // Constructor with message only
    public AitkenJuniorException(String message) {
        super(message);
        this.errorCode = 0; // default error code
    }

    // Constructor with message and cause
    public AitkenJuniorException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 0; // default error code
    }

    // Getter for the error code
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("message", this.getMessage());
        jsonMap.put("errorCode", this.getErrorCode());
        jsonMap.put("stackTrace", this.getCause() != null ? this.getCause().toString() : "N/A");

        try {
            return mapper.writeValueAsString(jsonMap);
        } catch (Exception e) {
            log.error("Error parsing exception. Code: {}, message: {}", this.getErrorCode(), this.getMessage());
            return "{\"message\":\"Error serializing exception\",\"errorCode\":-1}";
        }
    }
}
