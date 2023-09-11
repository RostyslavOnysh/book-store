package spring.boot.bookstore.mapper;

import org.mapstruct.Mapper;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.book.BookResponseDto;
import spring.boot.bookstore.dto.book.CreateBookRequestDto;
import spring.boot.bookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookResponseDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
