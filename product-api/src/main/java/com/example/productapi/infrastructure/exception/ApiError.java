package com.example.productapi.infrastructure.exception;

import java.time.Instant;

public record ApiError(Instant timestamp, int status, String error, String path) {

    public static ApiError of(int status, String error, String path) {
        return new ApiError(Instant.now(), status, error, path);
    }

}
