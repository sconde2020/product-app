package com.example.productapi.application.mapper;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        return Product.builder()
                .name(dto.name())
                .price(dto.price())
                .description(dto.description())
                .quantity(dto.quantity())
                .category(categoryMapper.toEntity(dto.category()))
                .build();
    }

    public ProductDto toDto(Product entity) {
        if (entity == null) return null;

        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                categoryMapper.toDto(entity.getCategory()),
                entity.getDescription(),
                entity.getQuantity()
        );
    }

    public List<ProductDto> toDtoList(List<Product> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Product> toEntityList(List<ProductDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

