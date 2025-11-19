package com.example.productapi.application.service;

import com.example.productapi.application.dto.CategoryDto;
import com.example.productapi.application.mapper.CategoryMapper;
import com.example.productapi.domain.exception.CategoryNotFoundException;
import com.example.productapi.domain.model.Category;
import com.example.productapi.domain.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryApplicationService {

    CategoryRepository repository;
    CategoryMapper mapper;

    public CategoryDto create(CategoryDto dto) {
        Category saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    public List<CategoryDto> getAll() {
        return repository.findAllByOrderByNameAsc().stream().map(mapper::toDto).toList();
    }

    public void delete(String code) {
        Category category = repository.findById(code)
                .orElseThrow(() -> new CategoryNotFoundException(code));
        repository.delete(category);
    }
}
