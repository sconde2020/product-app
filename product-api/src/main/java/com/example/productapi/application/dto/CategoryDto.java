package com.example.productapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.example.productapi.application.constants.ValidationMessages.*;

public record CategoryDto(
        @NotBlank(message = CATEGORY_CODE_EMPTY)
        @Size(max = 50, message = CATEGORY_CODE_TOO_LONG)
        String code,

        @NotBlank(message = CATEGORY_NAME_EMPTY)
        @Size(max = 100, message = CATEGORY_NAME_TOO_LONG)
        String name
) { }
