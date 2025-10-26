package com.example.productapi.integration;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.domain.model.Product;
import com.example.productapi.domain.repository.ProductRepository;
import com.example.productapi.infrastructure.repository.JpaProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ((JpaProductRepository) repository).deleteAll();
    }

    // ----------------- CREATE -----------------
    @Test
    void shouldCreateProduct_givenValidDto_whenPostProduct() throws Exception {
        ProductDto dto = new ProductDto(null, "Laptop", BigDecimal.valueOf(1200));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200));
    }

    @Test
    void shouldReturnBadRequest_givenInvalidDto_whenPostProduct() throws Exception {
        ProductDto dto = new ProductDto(null, "", BigDecimal.valueOf(-5));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ----------------- GET BY ID -----------------
    @Test
    void shouldReturnProductDto_givenExistingId_whenGetProduct() throws Exception {
        Product product = repository.save(new Product("TV", BigDecimal.valueOf(500)));

        mockMvc.perform(get("/api/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("TV"))
                .andExpect(jsonPath("$.price").value(500));
    }

    @Test
    void shouldReturnNotFound_givenNonExistingId_whenGetProduct() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    // ----------------- GET ALL WITH PAGINATION -----------------
    @Test
    void shouldReturnPagedProducts_givenProductsExist_whenGetAll() throws Exception {
        repository.save(new Product("A", BigDecimal.valueOf(10)));
        repository.save(new Product("B", BigDecimal.valueOf(20)));

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("A"))
                .andExpect(jsonPath("$.content[1].name").value("B"));
    }

    // ----------------- UPDATE -----------------
    @Test
    void shouldUpdateProduct_givenExistingId_whenPutProduct() throws Exception {
        Product product = repository.save(new Product("Old", BigDecimal.valueOf(50)));
        ProductDto dto = new ProductDto(null, "New", BigDecimal.valueOf(100));

        mockMvc.perform(put("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    void shouldReturnNotFound_givenNonExistingId_whenPutProduct() throws Exception {
        ProductDto dto = new ProductDto(null, "New", BigDecimal.valueOf(100));

        mockMvc.perform(put("/api/products/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // ----------------- DELETE -----------------
    @Test
    void shouldDeleteProduct_givenExistingId_whenDeleteProduct() throws Exception {
        Product product = repository.save(new Product("Item", BigDecimal.valueOf(50)));

        mockMvc.perform(delete("/api/products/{id}", product.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFound_givenNonExistingId_whenDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
