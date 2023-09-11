package spring.boot.bookstore.service.book.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.dto.book.BookResponseDto;
import spring.boot.bookstore.dto.book.BookSearchParameter;
import spring.boot.bookstore.dto.book.CreateBookRequestDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.mapper.BookMapper;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.repository.BookRepository;
import spring.boot.bookstore.repository.specification.book.builders.BookSpecificationBuilder;
import spring.boot.bookstore.service.book.BookService;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LogManager.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookResponseDto createBook(CreateBookRequestDto requestDto) {
        logger.info("Creating a new book: {}", requestDto.getTitle());
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
        
    @Override
    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find books by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookResponseDto> getBookByTitle(String title) {
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
    public BookResponseDto updateBook(Long id, CreateBookRequestDto bookRequestDto) {
        Book bookById = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find books by id: " + id)
        );
        bookById.setTitle(bookRequestDto.getTitle());
        bookById.setAuthor(bookRequestDto.getAuthor());
        bookById.setIsbn(bookRequestDto.getIsbn());
        bookById.setPrice(bookRequestDto.getPrice());
        bookById.setDescription(bookRequestDto.getDescription());
        bookById.setCoverImage(bookRequestDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(bookById));
    }

    @Override
    public List<BookResponseDto> searchBooks(BookSearchParameter searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
