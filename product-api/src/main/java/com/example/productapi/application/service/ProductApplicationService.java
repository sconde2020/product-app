package com.example.productapi.application.service;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.application.exception.InvalidProductException;
import com.example.productapi.application.mapper.ProductMapper;
import com.example.productapi.domain.exception.ProductNotFoundException;
import com.example.productapi.domain.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductApplicationService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductApplicationService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProductDto create(ProductDto dto) {
        validate(dto);
        var saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    public ProductDto findById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return mapper.toDto(entity);
    }

    public Page<ProductDto> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public ProductDto update(Long id, ProductDto dto) {
        var existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existing.setName(dto.name());
        existing.setPrice(dto.price());
        var updated = repository.save(existing);

        return mapper.toDto(updated);
    }

    public void delete(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private void validate(ProductDto dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new InvalidProductException("Name cannot be empty");
        }
        if (dto.price() == null || dto.price().doubleValue() <= 0) {
            throw new InvalidProductException("Price must be positive");
        }
    }
}
