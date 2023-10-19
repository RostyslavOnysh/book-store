package spring.boot.bookstore.service.category.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import spring.boot.bookstore.dto.category.CategoryRequestDto;
import spring.boot.bookstore.dto.category.CategoryResponseDto;
import spring.boot.bookstore.mapper.CategoryMapper;
import spring.boot.bookstore.model.Category;
import spring.boot.bookstore.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class CategoryServiceImplTest {
    private static final CategoryRequestDto REQUEST_DTO = new CategoryRequestDto();
    private static final Category VALID_CATEGORY = new Category();
    private static final CategoryResponseDto VALID_RESPONSE = new CategoryResponseDto();
    private static final Long CATEGORY_ID = 1L;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        REQUEST_DTO.setName("Fiction");
        REQUEST_DTO.setDescription("literature created from the imagination, not presented as fact,"
                + " though it may be based on a true story or situation");
        VALID_CATEGORY.setId(CATEGORY_ID);
        VALID_CATEGORY.setName("Fiction");
        VALID_CATEGORY.setDescription("literature created from the imagination,"
                + " not presented as fact,"
                + " though it may be based on a true story or situation");
        VALID_RESPONSE.setId(VALID_CATEGORY.getId());
        VALID_RESPONSE.setName(VALID_CATEGORY.getName());
        VALID_RESPONSE.setDescription(VALID_CATEGORY.getDescription());
    }

    @Test
    @DisplayName("createCategory_validPayload_True")
    void createCategory_validPayload_True() {
        when(categoryMapper.toEntity(REQUEST_DTO)).thenReturn(VALID_CATEGORY);
        when(categoryRepository.save(VALID_CATEGORY)).thenReturn(VALID_CATEGORY);
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_RESPONSE);
        CategoryResponseDto savedCategoryDto = categoryService.save(REQUEST_DTO);
        assertEquals(VALID_RESPONSE, savedCategoryDto);
        verify(categoryRepository, times(1)).save(VALID_CATEGORY);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("getAll_emptyCategoryList_False")
    void getAll_emptyCategoryList_False() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> emptyCategoryPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(categoryRepository.findAll(pageable)).thenReturn(emptyCategoryPage);
        List<CategoryResponseDto> actual = categoryService.findAll(pageable);
        assertTrue(actual.isEmpty());
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getCategoryById_validCategoryId_True")
    void getCategoryById_validCategoryId_True() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(VALID_CATEGORY));
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_RESPONSE);
        CategoryResponseDto actual = categoryService.getById(CATEGORY_ID);
        assertEquals(VALID_RESPONSE, actual);
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(categoryMapper).toDto(VALID_CATEGORY);
    }

    @Test
    @DisplayName("updateCategory_validCategoryIdAndPayload_True")
    void updateCategory_validCategoryIdAndPayload_True() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(VALID_CATEGORY));
        when(categoryMapper.toEntity(REQUEST_DTO)).thenReturn(VALID_CATEGORY);
        when(categoryRepository.save(VALID_CATEGORY)).thenReturn(VALID_CATEGORY);
        when(categoryMapper.toDto(VALID_CATEGORY)).thenReturn(VALID_RESPONSE);
        CategoryResponseDto updatedCategoryDto = categoryService.update(CATEGORY_ID, REQUEST_DTO);
        assertEquals(VALID_RESPONSE, updatedCategoryDto);
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(categoryMapper).toEntity(REQUEST_DTO);
        verify(categoryRepository).save(VALID_CATEGORY);
        verify(categoryMapper).toDto(VALID_CATEGORY);
    }

    @Test
    @DisplayName("deleteCategory_validCategoryId_True")
    void deleteCategory_validCategoryId_True() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(VALID_CATEGORY));
        doNothing().when(categoryRepository).delete(VALID_CATEGORY);
        assertDoesNotThrow(() -> categoryService.deleteById(CATEGORY_ID));
        verify(categoryRepository).delete(VALID_CATEGORY);
        verify(categoryRepository).findById(CATEGORY_ID);
    }
}
