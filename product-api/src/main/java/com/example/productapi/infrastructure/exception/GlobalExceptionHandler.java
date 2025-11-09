package com.example.productapi.infrastructure.exception;

import com.example.productapi.application.exception.InvalidProductException;
import com.example.productapi.domain.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ProductNotFoundException ex, HttpServletRequest req) {
        log.error("An error at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ApiError> handleInvalid(InvalidProductException ex, HttpServletRequest req) {
        log.error("An error at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = "Invalid product to add. Verify product detail.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, req.getRequestURI()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.error("An error at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = String.format("HTTP method '%s' is not supported for this endpoint. Supported methods: %s",
                ex.getMethod(), ex.getSupportedHttpMethods());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiError.of(405, msg, req.getRequestURI()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        log.error("An error at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = String.format("Missing required parameter '%s' of type '%s'",
                ex.getParameterName(), ex.getParameterType());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        log.error("An error at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = String.format("Parameter '%s' should be of type '%s'",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, req.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.error("An error at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = "Malformed JSON request or unreadable body.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(
                        500,
                        "An unexpected error occurred. Please contact support if the problem persists.",
                        req.getRequestURI()
                ));
    }

}
