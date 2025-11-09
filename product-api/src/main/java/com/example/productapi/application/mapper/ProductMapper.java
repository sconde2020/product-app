package com.example.productapi.application.mapper;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product entity = new Product();
        entity.setName(dto.name());
        entity.setPrice(dto.price());
        entity.setCategory(dto.category());
        entity.setDescription(dto.description());
        entity.setQuantity(dto.quantity());
        return entity;
    }

    public ProductDto toDto(Product entity) {
        if (entity == null) {
            return null;
        }

        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getQuantity()
        );
    }
}
