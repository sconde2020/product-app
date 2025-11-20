package com.example.productapi.web.controller;

import com.example.productapi.application.dto.CategoryDto;
import com.example.productapi.application.service.CategoryApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Crate, Read, Update, Delete operations for categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryApplicationService service;

    @Operation(summary = "Create a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category input")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryDto dto) {
        CategoryDto created = service.create(dto);
        return ResponseEntity
                .created(URI.create("/categories/" + created.code()))
                .body(created);
    }

    @Operation(summary = "Read all categories")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> readAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Update a category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category input"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{code}")
    public ResponseEntity<CategoryDto> update(@PathVariable String code, @RequestBody @Valid CategoryDto dto) {
        CategoryDto updated = service.update(code, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a category")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }
}
