package com.example.productapi.unit;

import com.example.productapi.application.dto.CategoryDto;
import com.example.productapi.application.dto.ProductDto;
import com.example.productapi.application.dto.ProductQueryParams;
import com.example.productapi.application.mapper.CategoryMapper;
import com.example.productapi.application.mapper.ProductMapper;
import com.example.productapi.application.service.ProductApplicationService;
import com.example.productapi.domain.exception.ProductNotFoundException;
import com.example.productapi.domain.model.Category;
import com.example.productapi.domain.model.Product;
import com.example.productapi.domain.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

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
        CategoryMapper categoryMapper = new CategoryMapper();
        mapper = new ProductMapper(categoryMapper);
        service = new ProductApplicationService(repository, mapper);
    }

    // ----------------- CREATE -----------------
    @Test
    void shouldCreateProduct_givenValidProductDto_whenCreate() {
        CategoryDto categoryDto = new CategoryDto("ELEC", "Electronics");
        ProductDto dto = new ProductDto(null, "Laptop", BigDecimal.valueOf(1200), categoryDto, "High-end laptop", 10);

        Product savedProduct = mapper.toEntity(dto);
        savedProduct.setId(1L);

        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDto result = service.create(dto);

        assertThat(result)
                .isNotNull()
                .extracting(ProductDto::id, ProductDto::name, p -> p.category().code(), p -> p.category().name(), ProductDto::description, ProductDto::quantity)
                .containsExactly(1L, "Laptop", "ELEC", "Electronics", "High-end laptop", 10);

        verify(repository, times(1)).save(any(Product.class));
    }

    // ----------------- FIND BY ID -----------------
    @Test
    void shouldReturnProductDto_givenExistingId_whenFindById() {
        Category category = Category.builder().code("ELEC").name("Electronics").build();
        Product product = new Product("TV", BigDecimal.valueOf(500), category, "LED TV", 3);
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto result = service.findById(1L);

        assertThat(result)
                .extracting(ProductDto::id, ProductDto::name, p -> p.category().code(), p -> p.category().name(), ProductDto::description, ProductDto::quantity)
                .containsExactly(1L, "TV", "ELEC", "Electronics", "LED TV", 3);
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
        Category cat1 = Category.builder().code("C1").name("Cat1").build();
        Category cat2 = Category.builder().code("C2").name("Cat2").build();

        Product product1 = new Product("A", BigDecimal.valueOf(10), cat1, "Desc1", 2);
        Product product2 = new Product("B", BigDecimal.valueOf(20), cat2, "Desc2", 5);

        Page<Product> page = new PageImpl<>(List.of(product1, product2));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        ProductQueryParams params = new ProductQueryParams(
                0, 2, "name", "asc", null, null, List.of("C1","C2"), null
        );

        Page<ProductDto> result = service.getAll(params);

        assertThat(result.getContent())
                .hasSize(2)
                .extracting(ProductDto::name, p -> p.category().code())
                .containsExactly(
                        tuple("A", "C1"),
                        tuple("B", "C2")
                );
    }

    // ----------------- UPDATE -----------------
    @Test
    void shouldUpdateProduct_givenExistingIdAndValidDto_whenUpdate() {
        Category existingCat = Category.builder().code("OLD").name("OldCat").build();
        Product existing = new Product("Old", BigDecimal.valueOf(50), existingCat, "OldDesc", 2);
        existing.setId(1L);

        CategoryDto newCatDto = new CategoryDto("NEW", "NewCat");
        ProductDto updateDto = new ProductDto(null, "New", BigDecimal.valueOf(100), newCatDto, "UpdatedDesc", 7);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(mapper.toEntity(updateDto));

        ProductDto result = service.update(1L, updateDto);

        assertThat(result)
                .extracting(ProductDto::name, ProductDto::price, p -> p.category().code(), p -> p.category().name(), ProductDto::description, ProductDto::quantity)
                .containsExactly("New", BigDecimal.valueOf(100), "NEW", "NewCat", "UpdatedDesc", 7);
    }

    @Test
    void shouldThrowProductNotFoundException_givenNonExistingId_whenUpdate() {
        CategoryDto dto = new CategoryDto("NEW", "NewCat");
        ProductDto productDto = new ProductDto(null, "New", BigDecimal.valueOf(100), dto, "UpdatedDesc", 12);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(1L, productDto))
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

