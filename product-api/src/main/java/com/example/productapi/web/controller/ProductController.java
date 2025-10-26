package com.example.productapi.web.controller;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.application.service.ProductApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "CRUD operations for products")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ProductController {

    private final ProductApplicationService service;

    public ProductController(ProductApplicationService service) {
        this.service = service;
    }


    @Operation(summary = "Create a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product input")
    })
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        var created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get a product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "List all products with pagination")
    @GetMapping
    public ResponseEntity<Page<ProductDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<ProductDto> result = service.getAll(page, size, sortBy, direction);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update a product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
