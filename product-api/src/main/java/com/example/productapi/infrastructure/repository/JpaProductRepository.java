package com.example.productapi.infrastructure.repository;

import com.example.productapi.domain.model.Product;
import com.example.productapi.domain.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository
        extends ProductRepository,
                    JpaRepository<Product, Long>,
                        JpaSpecificationExecutor<Product> { }
