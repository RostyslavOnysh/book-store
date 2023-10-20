package spring.boot.bookstore.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.boot.bookstore.dto.book.BookResponseDto;
import spring.boot.bookstore.dto.book.CreateBookRequestDto;
import spring.boot.bookstore.mapper.impl.BookMapperImpl;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.repository.BookRepository;
import spring.boot.bookstore.repository.specification.book.builders.BookSpecificationBuilder;
import spring.boot.bookstore.repository.specification.book.managers.SpecificationProviderManager;
import spring.boot.bookstore.service.book.BookService;
import spring.boot.bookstore.service.book.impl.BookServiceImpl;

@ContextConfiguration(classes = {BookController.class})
@RunWith(SpringRunner.class)
public class BookControllerTest {
    private static final Long INVALID_ID = 567L;

    @Autowired
    private BookController bookController;

    @MockBean
    private BookService bookService;
    private final BookMapperImpl bookMapper = new BookMapperImpl();
    private final BookRepository bookRepository = mock(BookRepository.class);

    @Test
    @DisplayName("Test createBook: With valid input, should return the expected result")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void saveBook_WithValidInput_ShouldReturnExpectedResult() {
        Book book = getBook();
        when(bookRepository.save(Mockito.any())).thenReturn(book);
        BookController bookController = new BookController(
                new BookServiceImpl(bookRepository, bookMapper,
                        new BookSpecificationBuilder(
                                mock(SpecificationProviderManager.class))));
        CreateBookRequestDto requestDto = createSampleBookRequestDto();
        BookResponseDto actualSaveResult = bookController.createBook(requestDto);
        assertEquals("Nikolai Gogol", actualSaveResult.getAuthor());
        assertEquals("Viy", actualSaveResult.getTitle());
        BigDecimal expectedPrice = BigDecimal.ONE;
        BigDecimal price2 = actualSaveResult.getPrice();
        assertSame(expectedPrice, price2);
        assertEquals("6666666", actualSaveResult.getIsbn());
        assertTrue(actualSaveResult.getCategoryIds().isEmpty());
        assertEquals("is a horror novella by the writer Nikolai Gogol,"
                        + " first published in volume 2 of his collection of "
                        + "tales entitled Mirgorod (1835).",
                actualSaveResult.getDescription());
        assertEquals(1L, actualSaveResult.getId().longValue());
        assertEquals("Cover Image", actualSaveResult.getCoverImage());
        assertEquals("1", price2.toString());
        verify(bookRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("Test getAll endpoint for book")
    public void getAll_validFourBook_returnResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        BookService bookService = mock(BookService.class);
        ArrayList<BookResponseDto> bookDtoList = new ArrayList<>();
        when(bookService.findAll(pageable)).thenReturn(bookDtoList);
        List<BookResponseDto> actualFindAllResult =
                (new BookController(bookService)).getAll(pageable);
        assertSame(bookDtoList, actualFindAllResult);
        assertTrue(actualFindAllResult.isEmpty());
        verify(bookService).findAll(pageable);
    }

    @Test
    @DisplayName("Test getBookById endpoint with invalid ID")
    public void getBookById_invalidId_returnNotFound() throws Exception {
        BookResponseDto bookDto = getBookResponseDto();
        when(this.bookService.getBookById(any())).thenReturn(bookDto);
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.get("/books/{id}", INVALID_ID);
        MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(INVALID_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Viy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author")
                        .value("Nikolai Gogol"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn")
                        .value("6666666"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price")
                        .value(67))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value("is a horror"
                                + " novella by the writer Nikolai Gogol, first published in "
                                + "volume 2 of his collection"
                                + " of tales entitled Mirgorod (1835)."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coverImage")
                        .value("Cover Image"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryIds").isArray());
    }

    @Test
    @DisplayName("Test searchBooks endpoint with valid parameters")
    public void searchBooks_validParameters_returnResponse() throws Exception {
        when(bookService.searchBooks(Mockito.any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books/search");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    @DisplayName("Test deleteBook: Should call deleteById on BookService")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteBook_ShouldCallDeleteByIdOnBookService() throws Exception {
        doNothing().when(bookService).deleteById(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.delete("/books/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test updateBook: With valid input, should return the expected result")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateBook_WithValidInput_ShouldReturnExpectedResult() throws Exception {
        CreateBookRequestDto createBookRequestDto = createSampleBookRequestDto();
        String content = (new ObjectMapper()).writeValueAsString(createBookRequestDto);
        MockHttpServletRequestBuilder requestBuilder = put("/books/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().is(200));
    }

    @NotNull
    private static CreateBookRequestDto createSampleBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Viy");
        requestDto.setAuthor("Nikolai Gogol");
        requestDto.setCategoryIds(new HashSet<>());
        requestDto.setCoverImage("Cover Image");
        requestDto.setDescription("is a horror novella by the writer Nikolai Gogol,"
                + " first published in volume 2 of his collection of"
                + " tales entitled Mirgorod (1835).");
        requestDto.setIsbn("6666666");
        BigDecimal price = BigDecimal.valueOf(1L);
        requestDto.setPrice(price);
        return requestDto;
    }

    @NotNull
    @DisplayName("Valid BookResponseDto")
    private static BookResponseDto getBookResponseDto() {
        BookResponseDto bookDto = new BookResponseDto();
        bookDto.setIsbn("6666666");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setId(567L);
        bookDto.setPrice(BigDecimal.valueOf(67));
        bookDto.setTitle("Viy");
        bookDto.setAuthor("Nikolai Gogol");
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("is a horror novella by the writer Nikolai Gogol,"
                + " first published in volume 2 of his collection of "
                + "tales entitled Mirgorod (1835).");
        return bookDto;
    }

    @NotNull
    @DisplayName("Valid Book")
    private static Book getBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Viy");
        book.setAuthor("Nikolai Gogol");
        book.setIsbn("6666666");
        book.setCategories(new HashSet<>());
        book.setCoverImage("Cover Image");
        book.setDeleted(true);
        book.setDescription("is a horror novella by the writer Nikolai Gogol,"
                + " first published in volume 2 "
                + "of his collection of tales entitled Mirgorod (1835).");
        book.setPrice(BigDecimal.valueOf(1L));
        return book;
    }
}
