package com.example.productapi.infrastructure.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        int status,
        String message,
        String path,
        Map<String, String> fieldErrors
) {

    public static ApiError of(int status, String message, String path) {
        return new ApiError(Instant.now(), status, message, path, Collections.emptyMap());
    }

    public static ApiError of(int status, String message, String path, Map<String, String> fieldErrors) {
        return new ApiError(Instant.now(), status, message, path, fieldErrors);
    }
}
