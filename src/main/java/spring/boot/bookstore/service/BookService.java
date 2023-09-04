package spring.boot.bookstore.service;

import java.util.List;
import spring.boot.bookstore.dto.BookSearchParameter;
import spring.boot.bookstore.dto.request.CreateBookRequestDto;
import spring.boot.bookstore.dto.response.BookDto;

public interface BookService {
    BookDto createBook(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    List<BookDto> getBookByTitle(String title);

    BookDto updateBook(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);

    List<BookDto> searchBooks(BookSearchParameter searchParameters);
}
