package spring.boot.bookstore.mapper;

import org.mapstruct.Mapper;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.request.CreateBookRequestDto;
import spring.boot.bookstore.dto.response.BookDto;
import spring.boot.bookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
