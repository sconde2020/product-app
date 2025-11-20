package com.example.productapi.integration;

import com.example.productapi.application.dto.CategoryDto;
import com.example.productapi.domain.model.Category;
import com.example.productapi.domain.repository.CategoryRepository;
import com.example.productapi.infrastructure.repository.JpaCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    Category savedCategory1;

    Category savedCategory2;

    @BeforeEach
    void setup() {
        savedCategory1 = Category.builder().code("COMP").name("Computers").build();
        savedCategory2 = Category.builder().code("ELEC").name("Electronics").build();
        repository.save(savedCategory1);
        repository.save(savedCategory2);
    }

    @AfterEach
    void clean() {
        ((JpaCategoryRepository) repository).deleteAll();
    }

    // ----------------- CREATE -----------------
    @Test
    void shouldCreateCategory_givenValidDto_whenPostCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto("MISC", "Miscellaneous");

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.code").value("MISC"))
                .andExpect(jsonPath("$.name").value("Miscellaneous"));

    }

    @Test
    void shouldReturnBadRequest_givenInvalidDto_whenPostCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto("", null);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist());

    }

    // ----------------- READ -----------------
    @Test
    void shouldReadAllCategories_givenCategoriesExist_whenGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code").value("COMP"))
                .andExpect(jsonPath("$[0].name").value("Computers"))
                .andExpect(jsonPath("$[1].code").value("ELEC"))
                .andExpect(jsonPath("$[1].name").value("Electronics"));
    }

    @Test
    void shouldReturnEmptyList_givenNoCategoriesExist_whenGetAllCategories() throws Exception {
        ((JpaCategoryRepository) repository).deleteAll();

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ----------------- UPDATE -----------------
    @Test
    void shouldUpdateCategory_givenValidDto_whenPutCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto(savedCategory1.getCode(), "Computer Systems");

        mockMvc.perform(put("/api/categories/{code}", savedCategory1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.code").value(savedCategory1.getCode()))
                .andExpect(jsonPath("$.name").value("Computer Systems"));
    }

    @Test
    void shouldReturnBadRequest_givenInvalidDto_whenPutCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto(savedCategory1.getCode(), null);

        mockMvc.perform(put("/api/categories/{code}", savedCategory1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist());

    }

    @Test
    void shouldReturnNotFound_givenCategoryNotExists_whenPutCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto("INVALID_CODE", "NEW VALUE");

        mockMvc.perform(put("/api/categories/{code}", "INVALID_CODE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist());

    }

    // ----------------- DELETE -----------------
    @Test
    void shouldDeleteCategory_givenExistingCode_whenDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/{code}", savedCategory2.getCode()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.code").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist());
    }

    @Test
    void shouldReturnNotFound_givenCategoryNotExists_whenDeleteCategory() throws Exception {
        String unSavedCode = "UNSAVED";

        mockMvc.perform(delete("/api/categories/{code}", unSavedCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist());

    }
}
