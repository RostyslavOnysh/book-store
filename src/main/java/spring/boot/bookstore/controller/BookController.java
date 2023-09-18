package spring.boot.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.bookstore.dto.book.BookResponseDto;
import spring.boot.bookstore.dto.book.BookSearchParameter;
import spring.boot.bookstore.dto.book.CreateBookRequestDto;
import spring.boot.bookstore.service.book.BookService;

@Tag(name = "Library management", description = "Endpoints for managing Library products")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private static final Logger logger = LogManager.getLogger(BookController.class);
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books from the library",
            description = "Retrieves a paginated list of all available books in the library."
                    + "Requires authentication for authorized access."
    )
    public List<BookResponseDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a book by ID",
            description = "Get book details using its unique identifier (ID). "
                    + "Requires authentication for authorized access."
    )
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new book",
            description = "Adds a new book to the library's collection."
                    + " Requires a valid book data payload in the request body."
                    + "Requires authentication for authorized access.")
    @PreAuthorize("hasRole('ADMIN')")
    public BookResponseDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        logger.info("Received a request to create a book: {}", requestDto.getTitle());
        return bookService.save(requestDto);
    }

    @GetMapping("/title")
    @Operation(summary = "Get books by title",
            description = "Retrieves a list of books in the library that match the specified title."
                    + "Requires authentication for authorized access."
    )
    public List<BookResponseDto> getAllByTitle(@RequestParam String title) {
        return bookService.getBookByTitle(title);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book by its ID",
            description = "Soft deletes a book from the library by its unique identifier (ID)."
                    + "Requires authentication for authorized access."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Update a book by its ID",
            description = "Updates an existing book in the library with new"
                    + " information based on its unique identifier (ID)."
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BookResponseDto updateById(@PathVariable Long id,
                                      @RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.updateBook(id, bookRequestDto);
    }

    @Operation(summary = "Search for books",
            description = "Searches for books in the library based on "
                    + "various search parameters such as title, author, or genre."
    )
    @GetMapping("/search")
    public List<BookResponseDto> searchBooks(BookSearchParameter searchParameters,
                                             Pageable pageable) {
        return bookService.searchBooks(searchParameters, pageable);
    }
}
