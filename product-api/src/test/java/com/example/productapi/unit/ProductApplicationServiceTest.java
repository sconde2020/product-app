package com.example.productapi.unit;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.application.exception.InvalidProductException;
import com.example.productapi.application.mapper.ProductMapper;
import com.example.productapi.application.service.ProductApplicationService;
import com.example.productapi.domain.exception.ProductNotFoundException;
import com.example.productapi.domain.model.Product;
import com.example.productapi.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductApplicationServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductApplicationService service;

    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ProductMapper();
        service = new ProductApplicationService(repository, mapper);
    }

    // ----------------- CREATE -----------------
    @Test
    void shouldCreateProduct_givenValidProductDto_whenCreate() {
        ProductDto dto = new ProductDto(null, "Laptop", BigDecimal.valueOf(1200));
        Product savedProduct = mapper.toEntity(dto);
        savedProduct.setId(1L);

        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDto result = service.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Laptop", result.name());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowInvalidProductException_givenEmptyName_whenCreate() {
        ProductDto dto = new ProductDto(null, "", BigDecimal.valueOf(100));

        InvalidProductException ex = assertThrows(InvalidProductException.class, () -> service.create(dto));
        assertEquals("Name cannot be empty", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidProductException_givenNegativePrice_whenCreate() {
        ProductDto dto = new ProductDto(null, "Phone", BigDecimal.valueOf(-5));

        InvalidProductException ex = assertThrows(InvalidProductException.class, () -> service.create(dto));
        assertEquals("Price must be positive", ex.getMessage());
        verify(repository, never()).save(any());
    }

    // ----------------- FIND BY ID -----------------
    @Test
    void shouldReturnProductDto_givenExistingId_whenFindById() {
        Product product = new Product("TV", BigDecimal.valueOf(500));
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = service.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("TV", result.name());
    }

    @Test
    void shouldThrowProductNotFoundException_givenNonExistingId_whenFindById() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.findById(1L));
    }

    // ----------------- GET ALL -----------------
    @Test
    void shouldReturnPagedProductDto_givenProductsExist_whenGetAll() {
        Product product1 = new Product("A", BigDecimal.valueOf(10));
        Product product2 = new Product("B", BigDecimal.valueOf(20));

        Page<Product> page = new PageImpl<>(List.of(product1, product2));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductDto> result = service.getAll(0, 2, "name", "ASC");

        assertEquals(2, result.getContent().size());
        assertEquals("A", result.getContent().get(0).name());
    }

    // ----------------- UPDATE -----------------
    @Test
    void shouldUpdateProduct_givenExistingIdAndValidDto_whenUpdate() {
        Product existing = new Product("Old", BigDecimal.valueOf(50));
        existing.setId(1L);

        ProductDto updateDto = new ProductDto(null, "New", BigDecimal.valueOf(100));
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ProductDto result = service.update(1L, updateDto);

        assertEquals("New", result.name());
        assertEquals(BigDecimal.valueOf(100), result.price());
    }

    @Test
    void shouldThrowProductNotFoundException_givenNonExistingId_whenUpdate() {
        ProductDto dto = new ProductDto(null, "New", BigDecimal.valueOf(100));
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.update(1L, dto));
    }

    // ----------------- DELETE -----------------
    @Test
    void shouldDeleteProduct_givenExistingId_whenDelete() {
        Product product = new Product("Item", BigDecimal.valueOf(50));
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.delete(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowProductNotFoundException_givenNonExistingId_whenDelete() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.delete(1L));
        verify(repository, never()).deleteById(1L);
    }
}
