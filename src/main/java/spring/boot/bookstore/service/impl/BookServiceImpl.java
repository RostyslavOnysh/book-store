package spring.boot.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.dto.BookSearchParameter;
import spring.boot.bookstore.dto.request.CreateBookRequestDto;
import spring.boot.bookstore.dto.response.BookDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.mapper.BookMapper;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.repository.BookRepository;
import spring.boot.bookstore.repository.specification.book.builders.BookSpecificationBuilder;
import spring.boot.bookstore.service.BookService;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find books by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> getBookByTitle(String title) {
        return bookRepository.findAllByTitleContainsIgnoreCase(title)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
        book.setDeleted(true);
        bookRepository.save(book);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto bookRequestDto) {
        Book updatedBook = bookMapper.toModel(bookRequestDto);
        updatedBook.setId(id);
        return bookMapper.toDto(bookRepository.save(updatedBook));
    }

    @Override
    public List<BookDto> searchBooks(BookSearchParameter searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
