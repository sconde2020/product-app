package com.example.productapi.domain.repository;

import com.example.productapi.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    List<Category> findAllByOrderByNameAsc();

    Optional<Category> findById(String code);

    void deleteById(String code);
}
