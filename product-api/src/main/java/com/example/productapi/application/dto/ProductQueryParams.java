package com.example.productapi.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductQueryParams(
        Integer page,
        Integer size,
        String sortBy,
        String direction,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        List<String> categoryCodes,
        String name
) {
    public ProductQueryParams {
        // Assign defaults if null
        page = (page == null) ? 0 : page;
        size = (size == null) ? 10 : size;
        sortBy = (sortBy == null) ? "id" : sortBy;
        direction = (direction == null) ? "asc" : direction;

        // Lists can be normalized
        categoryCodes = (categoryCodes == null) ? List.of() : categoryCodes;
    }
}

