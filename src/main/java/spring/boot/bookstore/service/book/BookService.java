package spring.boot.bookstore.service.book;

import java.util.List;
import org.springframework.data.domain.Pageable;
import spring.boot.bookstore.dto.book.BookResponseDto;
import spring.boot.bookstore.dto.book.BookSearchParameter;
import spring.boot.bookstore.dto.book.CreateBookRequestDto;

public interface BookService {
    BookResponseDto createBook(CreateBookRequestDto requestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto getBookById(Long id);

    List<BookResponseDto> getBookByTitle(String title);

    BookResponseDto updateBook(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);

    List<BookResponseDto> searchBooks(BookSearchParameter searchParameters);
}
