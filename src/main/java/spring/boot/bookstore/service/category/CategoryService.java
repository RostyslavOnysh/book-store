package spring.boot.bookstore.service.category;

import java.util.List;
import org.springframework.data.domain.Pageable;
import spring.boot.bookstore.dto.category.CategoryRequestDto;
import spring.boot.bookstore.dto.category.CategoryResponseDto;

public interface CategoryService {

    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);

    void deleteById(Long id);
}
