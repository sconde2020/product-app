package com.example.productapi.application.mapper;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductDto dto) {
        return new Product(dto.name(), dto.price());
    }

    public ProductDto toDto(Product entity) {
        return new ProductDto(entity.getId(), entity.getName(), entity.getPrice());
    }
}