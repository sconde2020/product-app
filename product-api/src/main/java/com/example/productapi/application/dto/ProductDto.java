package com.example.productapi.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import static com.example.productapi.application.constants.ProductSize.*;
import static com.example.productapi.application.constants.ValidationMessages.*;


public record ProductDto(
        @Nullable
        @Min(value = 0, message = ID_BAD_VALUE)
        Long id,

        @NotBlank(message = NAME_EMPTY)
        @Size(max = NAME_SIZE, message = NAME_TOO_LONG)
        String name,

        @NotNull(message = PRICE_EMPTY)
        @DecimalMin(value = "0.1", message = PRICE_BAD_VALUE)
        BigDecimal price,

        @NotNull(message = CATEGORY_EMPTY)
        @Valid
        CategoryDto category,

        @NotBlank(message = DESCRIPTION_EMPTY)
        @Size(max = DESCRIPTION_SIZE, message = DESCRIPTION_TOO_LONG)
        String description,

        @NotNull(message = QUANTITY_EMPTY)
        @Min(value = 1, message = QUANTITY_BAD_VALUE)
        Integer quantity
) { }
