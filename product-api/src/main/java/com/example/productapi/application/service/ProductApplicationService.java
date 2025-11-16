package com.example.productapi.application.service;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.application.mapper.ProductMapper;
import com.example.productapi.domain.exception.ProductNotFoundException;
import com.example.productapi.domain.model.Product;
import com.example.productapi.domain.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductApplicationService {

    ProductRepository repository;
    ProductMapper mapper;

    public ProductApplicationService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProductDto create(ProductDto dto) {
        Product saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    public ProductDto findById(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return mapper.toDto(entity);
    }

    public Page<ProductDto> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public ProductDto update(Long id, ProductDto dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Product newProduct = mapper.toEntity(dto);
        newProduct.setId(existing.getId());

        Product updated = repository.save(newProduct);

        return mapper.toDto(updated);
    }

    public void delete(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
