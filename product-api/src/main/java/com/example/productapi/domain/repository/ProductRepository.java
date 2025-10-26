package com.example.productapi.domain.repository;

import com.example.productapi.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    void deleteById(Long id);

    Page<Product> findAll(Pageable pageable);

}
