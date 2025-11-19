package com.example.productapi.infrastructure.exception;

import com.example.productapi.application.exception.InvalidProductException;
import com.example.productapi.domain.exception.CategoryNotFoundException;
import com.example.productapi.domain.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // --- Custom exceptions ---

    @ExceptionHandler({ ProductNotFoundException.class, CategoryNotFoundException.class })
    public ResponseEntity<ApiError> handleNotFound(ProductNotFoundException ex, HttpServletRequest req) {
        log.error("Not found at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ApiError> handleInvalidProduct(InvalidProductException ex, HttpServletRequest req) {
        log.error("Invalid product at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = "Invalid product data. Verify product details.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, req.getRequestURI()));
    }

    // --- Bean Validation errors ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest req) {
        log.error("Validation failed at {}: {}", req.getRequestURI(), ex.getMessage(), ex);

        // Map of field -> error message
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "invalid"),
                        (existing, replacement) -> existing
                ));


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, "Validation failed for one or more fields", req.getRequestURI(), fieldErrors));
    }

    // --- Other common Spring MVC exceptions ---

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.error("Method not allowed at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = String.format("HTTP method '%s' is not supported for this endpoint. Supported methods: %s",
                ex.getMethod(), ex.getSupportedHttpMethods());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiError.of(405, msg, req.getRequestURI()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        log.error("Missing parameter at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = String.format("Missing required parameter '%s' of type '%s'",
                ex.getParameterName(), ex.getParameterType());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        log.error("Type mismatch at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = String.format("Parameter '%s' should be of type '%s'",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, req.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.error("Malformed JSON at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        String msg = "Malformed JSON request or unreadable body.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, req.getRequestURI()));
    }

    // --- Catch-all ---

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


