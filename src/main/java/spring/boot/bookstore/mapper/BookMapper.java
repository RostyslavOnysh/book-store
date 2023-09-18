package spring.boot.bookstore.mapper;

import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.book.BookResponseDto;
import spring.boot.bookstore.dto.book.BookWithoutCategoryIdsDto;
import spring.boot.bookstore.dto.book.CreateBookRequestDto;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookResponseDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);

    BookWithoutCategoryIdsDto toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookResponseDto bookResponseDto, Book book) {
        bookResponseDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }
}
