package com.example.productapi.unit;

import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.application.exception.InvalidProductException;
import com.example.productapi.application.mapper.ProductMapper;
import com.example.productapi.application.service.ProductApplicationService;
import com.example.productapi.domain.exception.ProductNotFoundException;
import com.example.productapi.domain.model.Product;
import com.example.productapi.domain.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductApplicationServiceTest {

    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductApplicationService service;

    ProductMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ProductMapper();
        service = new ProductApplicationService(repository, mapper);
    }

    // ----------------- CREATE -----------------
    @Test
    void shouldCreateProduct_givenValidProductDto_whenCreate() {
        ProductDto dto = new ProductDto(null, "Laptop", BigDecimal.valueOf(1200), "Electronics", "High-end laptop", 10);
        Product savedProduct = mapper.toEntity(dto);
        savedProduct.setId(1L);

        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDto result = service.create(dto);

        assertThat(result)
                .isNotNull()
                .extracting(ProductDto::id, ProductDto::name, ProductDto::category, ProductDto::description, ProductDto::quantity)
                .containsExactly(1L, "Laptop", "Electronics", "High-end laptop", 10);

        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowInvalidProductException_givenEmptyName_whenCreate() {
        ProductDto dto = new ProductDto(null, "", BigDecimal.valueOf(100), "Test", "Desc", 1);

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("Name cannot be empty");

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidProductException_givenNegativePrice_whenCreate() {
        ProductDto dto = new ProductDto(null, "Phone", BigDecimal.valueOf(-5), "Electronics", "Smartphone", 5);

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("Price must be positive");

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidProductException_givenEmptyCategory_whenCreate() {
        ProductDto dto = new ProductDto(null, "Phone", BigDecimal.valueOf(5), "", "Smartphone", 5);

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("Category cannot be empty");

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidProductException_givenEmptyDescription_whenCreate() {
        ProductDto dto = new ProductDto(null, "Phone", BigDecimal.valueOf(5), "Electronics", null, 5);

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("Description cannot be empty");

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidProductException_givenNullQuantity_whenCreate() {
        ProductDto dto = new ProductDto(null, "Phone", BigDecimal.valueOf(6), "Electronics", "Smartphone", 0);

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("Quantity must be positive");

        verify(repository, never()).save(any());
    }

    // ----------------- FIND BY ID -----------------
    @Test
    void shouldReturnProductDto_givenExistingId_whenFindById() {
        Product product = new Product("TV", BigDecimal.valueOf(500));
        product.setId(1L);
        product.setCategory("Electronics");
        product.setDescription("LED TV");
        product.setQuantity(3);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = service.findById(1L);

        assertThat(result)
                .extracting(ProductDto::id, ProductDto::name, ProductDto::category, ProductDto::description, ProductDto::quantity)
                .containsExactly(1L, "TV", "Electronics", "LED TV", 3);
    }

    @Test
    void shouldThrowProductNotFoundException_givenNonExistingId_whenFindById() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    // ----------------- GET ALL -----------------
    @Test
    void shouldReturnPagedProductDto_givenProductsExist_whenGetAll() {
        Product product1 = new Product("A", BigDecimal.valueOf(10));
        product1.setCategory("Cat1");
        product1.setDescription("Desc1");
        product1.setQuantity(2);

        Product product2 = new Product("B", BigDecimal.valueOf(20));
        product2.setCategory("Cat2");
        product2.setDescription("Desc2");
        product2.setQuantity(5);

        Page<Product> page = new PageImpl<>(List.of(product1, product2));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductDto> result = service.getAll(0, 2, "name", "ASC");

        assertThat(result.getContent())
                .hasSize(2)
                .extracting(ProductDto::name, ProductDto::category)
                .containsExactly(
                        tuple("A", "Cat1"),
                        tuple("B", "Cat2")
                );
    }

    // ----------------- UPDATE -----------------
    @Test
    void shouldUpdateProduct_givenExistingIdAndValidDto_whenUpdate() {
        Product existing = new Product("Old", BigDecimal.valueOf(50));
        existing.setId(1L);
        existing.setCategory("OldCat");
        existing.setDescription("OldDesc");
        existing.setQuantity(2);

        ProductDto updateDto = new ProductDto(null, "New", BigDecimal.valueOf(100), "NewCat", "UpdatedDesc", 7);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(mapper.toEntity(updateDto));

        ProductDto result = service.update(1L, updateDto);

        assertThat(result)
                .extracting(ProductDto::name, ProductDto::price, ProductDto::category, ProductDto::description, ProductDto::quantity)
                .containsExactly("New", BigDecimal.valueOf(100), "NewCat", "UpdatedDesc", 7);
    }

    @Test
    void shouldThrowProductNotFoundException_givenNonExistingId_whenUpdate() {
        ProductDto dto = new ProductDto(null, "New", BigDecimal.valueOf(100), "Cat", "Desc", 3);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(1L, dto))
                .isInstanceOf(ProductNotFoundException.class);
    }

    // ----------------- DELETE -----------------
    @Test
    void shouldDeleteProduct_givenExistingId_whenDelete() {
        Product product = new Product("Item", BigDecimal.valueOf(50));
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(repository).deleteById(1L);

        assertThatCode(() -> service.delete(1L))
                .doesNotThrowAnyException();

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowProductNotFoundException_givenNonExistingId_whenDelete() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(ProductNotFoundException.class);

        verify(repository, never()).deleteById(1L);
    }
}
