package spring.boot.bookstore.service.book.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import spring.boot.bookstore.dto.book.BookResponseDto;
import spring.boot.bookstore.dto.book.CreateBookRequestDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.mapper.BookMapper;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.model.Category;
import spring.boot.bookstore.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class BookServiceImplTest {
    private static final CreateBookRequestDto REQUEST_DTO = new CreateBookRequestDto();
    private static final Book VALID_BOOK = new Book();
    private static final BookResponseDto VALID_RESPONSE = new BookResponseDto();
    private static final Category VALID_CATEGORY = new Category();
    private static final Long VALID_CATEGORY_ID = 1L;
    private static final Long VALID_BOOK_ID = 1L;
    private static final Long INVALID_BOOK_ID = 5678L;
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        REQUEST_DTO.setTitle("Eneyida");
        REQUEST_DTO.setAuthor("Ivan Kotliarevsky");
        REQUEST_DTO.setPrice(BigDecimal.ONE);
        REQUEST_DTO.setDescription("Ukrainian burlesque poem, written"
                + " by Ivan Kotliarevsky in 1798");
        REQUEST_DTO.setCategoryIds(Set.of(VALID_CATEGORY_ID));
        REQUEST_DTO.setIsbn("978-0-13-516630-7");
        VALID_BOOK.setId(VALID_CATEGORY_ID);
        VALID_BOOK.setCategories(Set.of(VALID_CATEGORY));
        VALID_BOOK.setPrice(REQUEST_DTO.getPrice());
        VALID_BOOK.setIsbn(REQUEST_DTO.getIsbn());
        VALID_BOOK.setAuthor(REQUEST_DTO.getAuthor());
        VALID_BOOK.setTitle(REQUEST_DTO.getTitle());
        VALID_BOOK.setDescription(REQUEST_DTO.getDescription());
        VALID_BOOK.setCoverImage(REQUEST_DTO.getCoverImage());
        VALID_RESPONSE.setId(VALID_BOOK.getId());
        VALID_RESPONSE.setTitle(VALID_BOOK.getTitle());
        VALID_RESPONSE.setAuthor(VALID_BOOK.getAuthor());
        VALID_RESPONSE.setPrice(VALID_BOOK.getPrice());
        VALID_RESPONSE.setIsbn(VALID_BOOK.getIsbn());
        VALID_RESPONSE.setDescription(VALID_BOOK.getDescription());
        VALID_RESPONSE.setCoverImage(VALID_BOOK.getCoverImage());
        Set<Long> categoriesId = VALID_BOOK.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        VALID_RESPONSE.setCategoryIds(categoriesId);
    }

    @Test
    @DisplayName("save_ValidBook_Success")
    void save_ValidBook_Success() {
        when(bookMapper.toModel(REQUEST_DTO)).thenReturn(VALID_BOOK);
        when(bookRepository.save(VALID_BOOK)).thenReturn(VALID_BOOK);
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_RESPONSE);
        BookResponseDto savedBookDto = bookService.save(REQUEST_DTO);
        assertEquals(VALID_RESPONSE, savedBookDto);
        verify(bookRepository, times(1)).save(VALID_BOOK);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("findAll_BooksExist_ReturnsAllBooks")
    void findAll_BooksExist_ReturnsAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findAllWithCategories(pageable))
                .thenReturn(Collections.singletonList(VALID_BOOK));
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_RESPONSE);
        List<BookResponseDto> actual = bookService.findAll(pageable);
        verify(bookRepository).findAllWithCategories(pageable);
        verify(bookMapper).toDto(VALID_BOOK);
        assertEquals(Collections.singletonList(VALID_RESPONSE), actual);
    }

    @Test
    @DisplayName("getBookById_ExistingId_ReturnsBook")
    void getBookById_ExistingId_ReturnsBook() {
        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(VALID_BOOK));
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_RESPONSE);
        BookResponseDto actual = bookService.getBookById(VALID_BOOK_ID);
        verify(bookRepository).findById(VALID_BOOK_ID);
        verify(bookMapper).toDto(VALID_BOOK);
        assertEquals(VALID_RESPONSE, actual);
    }

    @Test
    @DisplayName("getBookByTitle_ExistingTitle_ReturnsBook")
    void getBookByTitle_ExistingTitle_ReturnsBook() {
        List<Book> books = new ArrayList<>();
        books.add(VALID_BOOK);
        when(bookRepository
                .findAllByTitleContainsIgnoreCase(REQUEST_DTO.getTitle())).thenReturn(books);
        when(bookMapper.toDto(VALID_BOOK)).thenReturn(VALID_RESPONSE);
        List<BookResponseDto> result = bookService.getBookByTitle(REQUEST_DTO.getTitle());
        assertEquals(1, result.size());
        assertEquals(VALID_RESPONSE, result.get(0));
        verify(bookRepository).findAllByTitleContainsIgnoreCase(REQUEST_DTO.getTitle());
        verify(bookMapper, times(books.size())).toDto(VALID_BOOK);
    }

    @Test
    @DisplayName("getBookByTitle_EmptyTitle_ReturnsEmptyList")
    void getBookByTitle_EmptyTitle_ReturnsEmptyList() {
        String emptyTitle = "";
        List<BookResponseDto> returnedBooks = bookService.getBookByTitle(emptyTitle);
        assertThat(returnedBooks).isNotNull();
        assertThat(returnedBooks.isEmpty());
    }

    @Test
    @DisplayName("deleteById_ValidId_Success")
    void deleteById_ValidId_Success() {
        when(bookRepository.findById(VALID_BOOK_ID))
                .thenReturn(Optional.of(VALID_BOOK));
        assertDoesNotThrow(() -> bookService.deleteById(VALID_BOOK_ID));
        verify(bookRepository, times(1)).save(argThat(Book::isDeleted));
        verify(bookRepository, times(1)).findById(VALID_BOOK_ID);
    }

    @Test
    @DisplayName("Test deleteById with non-existing book ID")
    void deleteById_nonExistingBook_throwException() {
        when(bookRepository.findById(INVALID_BOOK_ID))
                .thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteById(INVALID_BOOK_ID));
        verify(bookRepository).findById(INVALID_BOOK_ID);
        assertEquals("Can't find book by id: " + INVALID_BOOK_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Test updateById with non-existing book")
    void updateById_nonExistingBook_throwException() {
        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(INVALID_BOOK_ID, REQUEST_DTO));
        verify(bookRepository).findById(INVALID_BOOK_ID);
        assertEquals("Can't find books by id: " + INVALID_BOOK_ID, exception.getMessage());
    }

    @Test
    @DisplayName("updateBook_NonExistingId_ThrowsEntityNotFoundException")
    void updateBook_NonExistingId_ThrowsEntityNotFoundException() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Kobzar");
        bookRequestDto.setAuthor("Taras Shevchenko");
        bookRequestDto.setIsbn("Updated ISBN");
        bookRequestDto.setPrice(BigDecimal.valueOf(29.99));
        bookRequestDto.setDescription("is a book of poems by Ukrainian poet "
                + "and painter Taras Shevchenko");
        bookRequestDto.setCoverImage("Updated Cover Image");
        when(bookRepository.findById(INVALID_BOOK_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(INVALID_BOOK_ID, bookRequestDto));
        assertEquals("Can't find books by id: " + INVALID_BOOK_ID, exception.getMessage());
        verify(bookRepository, times(1)).findById(INVALID_BOOK_ID);
        verifyNoMoreInteractions(bookRepository);
    }
}
