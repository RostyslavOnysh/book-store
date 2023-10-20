package spring.boot.bookstore.service.category.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.dto.category.CategoryRequestDto;
import spring.boot.bookstore.dto.category.CategoryResponseDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.mapper.CategoryMapper;
import spring.boot.bookstore.model.Category;
import spring.boot.bookstore.repository.CategoryRepository;
import spring.boot.bookstore.service.category.CategoryService;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryDto) {
        Category savedCategory = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
        Category categoryById = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find category by id: " + id));
        Category update = categoryMapper.toEntity(categoryRequestDto);
        update.setId(id);
        return categoryMapper.toDto(categoryRepository.save(update));
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found by ID :" + id));
        categoryRepository.delete(category);
    }
}
