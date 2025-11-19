package com.example.productapi.integration;

import com.example.productapi.application.dto.CategoryDto;
import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.domain.model.Category;
import com.example.productapi.domain.model.Product;
import com.example.productapi.domain.repository.CategoryRepository;
import com.example.productapi.domain.repository.ProductRepository;
import com.example.productapi.infrastructure.repository.JpaCategoryRepository;
import com.example.productapi.infrastructure.repository.JpaProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ((JpaProductRepository) repository).deleteAll();
        ((JpaCategoryRepository) categoryRepository).deleteAll();
    }

    // ----------------- CREATE -----------------
    @Test
    void shouldCreateProduct_givenValidDto_whenPostProduct() throws Exception {
        CategoryDto categoryDto = new CategoryDto("COMPUTERS", "Computers");
        categoryRepository.save(
                Category.builder()
                        .code(categoryDto.code())
                        .name(categoryDto.name())
                        .build()
        );

        ProductDto dto = new ProductDto(
                null,
                "Laptop Pro",
                BigDecimal.valueOf(1200),
                categoryDto,
                "High-end laptop for professionals",
                10
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.price").value(1200))
                .andExpect(jsonPath("$.category.code").value("COMPUTERS"))
                .andExpect(jsonPath("$.category.name").value("Computers"))
                .andExpect(jsonPath("$.description").value("High-end laptop for professionals"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void shouldReturnBadRequest_givenInvalidDto_whenPostProduct() throws Exception {
        ProductDto dto = new ProductDto(
                null,
                "",
                BigDecimal.valueOf(-5),
                null,
                "",
                -1
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ----------------- GET BY ID -----------------
    @Test
    void shouldReturnProductDto_givenExistingId_whenGetProduct() throws Exception {
        Category category = Category.builder().code("ELEC").name("Electronics").build();
        categoryRepository.save(category);

        Product product = repository.save(new Product(
                null,
                "TV",
                BigDecimal.valueOf(500),
                category,
                "4K Ultra HD Smart TV",
                5
        ));

        mockMvc.perform(get("/api/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("TV"))
                .andExpect(jsonPath("$.price").value(500))
                .andExpect(jsonPath("$.category.code").value("ELEC"))
                .andExpect(jsonPath("$.category.name").value("Electronics"))
                .andExpect(jsonPath("$.description").value("4K Ultra HD Smart TV"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void shouldReturnNotFound_givenNonExistingId_whenGetProduct() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    // ----------------- GET ALL WITH PAGINATION + FILTERS -----------------
    @Test
    void shouldReturnPagedProducts_givenProductsExist_whenGetAll() throws Exception {
        Category category = Category.builder().code("MISC").name("Misc").build();
        categoryRepository.save(category);
        repository.save(new Product(null, "A", BigDecimal.valueOf(10), category, "Cheap item", 15));
        repository.save(new Product(null, "B", BigDecimal.valueOf(20), category, "More expensive item", 10));

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc")
                        .param("categoryCodes", "MISC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("A"))
                .andExpect(jsonPath("$.content[1].name").value("B"));
    }

    // ----------------- UPDATE -----------------
    @Test
    void shouldUpdateProduct_givenExistingId_whenPutProduct() throws Exception {
        Category category = Category.builder().code("ACC").name("Accessories").build();
        categoryRepository.save(category);
        Product product = repository.save(new Product(
                null,
                "Old",
                BigDecimal.valueOf(50),
                category,
                "Old description",
                5
        ));

        ProductDto dto = new ProductDto(
                null,
                "New",
                BigDecimal.valueOf(100),
                new CategoryDto(category.getCode(), category.getName()),
                "Updated description",
                12
        );

        mockMvc.perform(put("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.category.code").value("ACC"))
                .andExpect(jsonPath("$.category.name").value("Accessories"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.quantity").value(12));
    }

    @Test
    void shouldReturnNotFound_givenNonExistingId_whenPutProduct() throws Exception {
        ProductDto dto = new ProductDto(
                null,
                "New",
                BigDecimal.valueOf(100),
                new CategoryDto("ACC", "Accessories"),
                "Updated description",
                12
        );

        mockMvc.perform(put("/api/products/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // ----------------- DELETE -----------------
    @Test
    void shouldDeleteProduct_givenExistingId_whenDeleteProduct() throws Exception {
        Category category = Category.builder().code("OFF").name("Office").build();
        categoryRepository.save(category);
        Product product = repository.save(new Product(
                null,
                "Item",
                BigDecimal.valueOf(50),
                category,
                "Something to delete",
                8
        ));

        mockMvc.perform(delete("/api/products/{id}", product.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFound_givenNonExistingId_whenDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}

