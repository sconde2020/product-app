package com.example.productapi.infrastructure.exception;

import java.time.Instant;

public record ApiError(Instant timestamp, int status, String message, String path) {

    public static ApiError of(int status, String message, String path) {
        return new ApiError(Instant.now(), status, message, path);
    }

}
