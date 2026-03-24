package org.zeroday.zarzadzaniezadaniami.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> validationErrors
) {
    public ApiError {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    public static ApiError of(HttpStatus status, String message) {
        return new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, null);
    }

    public static ApiError validation(HttpStatus status, String message, Map<String, String> errors) {
        return new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, errors);
    }
}
