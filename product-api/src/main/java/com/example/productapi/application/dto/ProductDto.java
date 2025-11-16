package com.example.productapi.application.dto;

import com.example.productapi.application.constants.ValidationMessages;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import static com.example.productapi.application.constants.ProductSize.*;

public record ProductDto(
        @Nullable
        @Min(value = 0, message = ValidationMessages.ID_BAD_VALUE)
        Long id,

        @NotBlank(message = ValidationMessages.NAME_EMPTY)
        @Size(max = NAME_SIZE, message = ValidationMessages.NAME_TOO_LONG)
        String name,

        @NotNull(message = ValidationMessages.PRICE_EMPTY)
        @DecimalMin(value = "0.1", message = ValidationMessages.PRICE_BAD_VALUE)
        BigDecimal price,

        @NotBlank(message = ValidationMessages.CATEGORY_EMPTY)
        @Size(max = CATEGORY_SIZE, message = ValidationMessages.CATEGORY_TOO_LONG)
        String category,

        @NotBlank(message = ValidationMessages.DESCRIPTION_EMPTY)
        @Size(max = DESCRIPTION_SIZE, message = ValidationMessages.DESCRIPTION_TOO_LONG)
        String description,

        @NotNull(message = ValidationMessages.QUANTITY_EMPTY)
        @Min(value = 1, message = ValidationMessages.QUANTITY_BAD_VALUE)
        Integer quantity
) { }
