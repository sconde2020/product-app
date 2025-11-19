package com.example.productapi.application.mapper;

import com.example.productapi.application.dto.CategoryDto;
import com.example.productapi.domain.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryDto dto) {
        if (dto == null) return null;
        return Category.builder()
                .code(dto.code())
                .name(dto.name())
                .build();
    }

    public CategoryDto toDto(Category entity) {
        if (entity == null) return null;
        return new CategoryDto(entity.getCode(), entity.getName());
    }
}

