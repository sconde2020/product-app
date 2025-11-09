package com.example.productapi.application.dto;

import java.math.BigDecimal;

public record ProductDto(Long id, String name, BigDecimal price, String category, String description, Integer quantity) { }
