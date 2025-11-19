package com.example.productapi.infrastructure.repository;

import com.example.productapi.domain.model.Category;
import com.example.productapi.domain.repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCategoryRepository extends CategoryRepository, JpaRepository<Category, String> {

    List<Category> findAllByOrderByNameAsc();

}
