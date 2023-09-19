package spring.boot.bookstore.mapper;

import org.mapstruct.Mapper;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.category.CategoryRequestDto;
import spring.boot.bookstore.dto.category.CategoryResponseDto;
import spring.boot.bookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryDto);
}
