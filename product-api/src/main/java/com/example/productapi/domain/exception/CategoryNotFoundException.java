package com.example.productapi.domain.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String code) {
        super(String.format("Category not found with code %s", code));
    }
}
