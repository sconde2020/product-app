package com.example.productapi.unit;

import com.example.productapi.application.dto.CategoryDto;
import com.example.productapi.application.mapper.CategoryMapper;
import com.example.productapi.application.service.CategoryApplicationService;
import com.example.productapi.domain.exception.CategoryNotFoundException;
import com.example.productapi.domain.model.Category;
import com.example.productapi.domain.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryApplicationServiceTest {

    @Mock
    CategoryRepository repository;

    @Mock
    CategoryMapper mapper;

    @InjectMocks
    CategoryApplicationService service;

    //----------CREATE--------------
    @Test
    void shouldCreateCategory_givenCategoryDto_whenCreate() {
        CategoryDto categoryDto = new CategoryDto("ELEC", "Electronics");
        Category category = Category.builder().code("ELEC").name("Electronics").build();

        when(mapper.toEntity(any(CategoryDto.class))).thenReturn(category);
        when(mapper.toDto(any(Category.class))).thenReturn(categoryDto);
        when(repository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = service.create(categoryDto);

        assertThat(result)
                .isNotNull()
                .extracting(CategoryDto::code, CategoryDto::name)
                .containsExactly("ELEC", "Electronics");

        verify(repository, times(1)).save(any(Category.class));
    }

    //----------GET ALL--------------
    @Test
    void shouldReadCategories_givenCategoriesExist_whenGetAll() {
        CategoryDto categoryDto1 = new CategoryDto("ELEC", "Electronics");
        Category category1 = Category.builder().code("ELEC").name("Electronics").build();

        CategoryDto categoryDto2 = new CategoryDto("COMP", "Computers");
        Category category2 = Category.builder().code("COMP").name("Computers").build();

        when(mapper.toDto(category1)).thenReturn(categoryDto1);
        when(mapper.toDto(category2)).thenReturn(categoryDto2);

        when(repository.findAllByOrderByNameAsc()).thenReturn(List.of(category2, category1));

        List<CategoryDto> result = service.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(categoryDto2, categoryDto1);
    }

    @Test
    void shouldReadEmptyList_givenNoneCategories_whenGetAll() {
        when(repository.findAllByOrderByNameAsc()).thenReturn(List.of());

        List<CategoryDto> result = service.getAll();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    //----------UPDATE--------------
    @Test
    void shouldUpdateCategory_givenExistingCode_whenUpdate() {
        String categoryCode = "MISC";

        Category existingCategory = Category.builder()
                .code(categoryCode)
                .name("Miscellaneous")
                .build();

        CategoryDto updateCategoryDto = new CategoryDto(categoryCode, "All sorts");
        Category updatedCategory = Category.builder()
                .code(categoryCode)
                .name("All sorts")
                .build();

        when(mapper.toEntity(updateCategoryDto)).thenReturn(updatedCategory);
        when(mapper.toDto(updatedCategory)).thenReturn(updateCategoryDto);

        when(repository.findById(categoryCode)).thenReturn(Optional.of(existingCategory));
        when(repository.save(updatedCategory)).thenReturn(updatedCategory);

        CategoryDto result = service.update(categoryCode, updateCategoryDto);

        assertThat(result)
                .isNotNull()
                .extracting(CategoryDto::code, CategoryDto::name)
                .containsExactly(categoryCode, "All sorts");

        verify(repository, times(1)).findById(categoryCode);
        verify(repository, times(1)).save(updatedCategory);
    }

    @Test
    void shouldThrowCategoryNotFoundException_givenNonExistentCode_whenUpdate() {
        String categoryCode = "UNSAVED";

        CategoryDto newCategoryDto = new CategoryDto(categoryCode, "All sorts");

        when(repository.findById(categoryCode)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(categoryCode, newCategoryDto))
                .isInstanceOf(CategoryNotFoundException.class);

        verify(repository, times(1)).findById(categoryCode);
    }

    //----------DELETE--------------
    @Test
    void shouldDeleteCategory_givenExistingCode_whenDelete() {
        String categoryCode = "KNOWN";

        Category deletedCategory = Category.builder()
                .code(categoryCode)
                .name("All known stuff")
                .build();

        when(repository.findById(categoryCode)).thenReturn(Optional.of(deletedCategory));

        assertThatCode(() -> service.delete(categoryCode)).doesNotThrowAnyException();

        verify(repository, times(1)).findById(categoryCode);
        verify(repository, times(1)).deleteById(categoryCode);
    }

    @Test
    void shouldReturnNotFound_givenNonExistentCode_whenDelete() {
        String categoryCode = "UNKNOWN";

        when(repository.findById(categoryCode)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(categoryCode))
                .isInstanceOf(CategoryNotFoundException.class);

        verify(repository, times(1)).findById(categoryCode);
    }
}
