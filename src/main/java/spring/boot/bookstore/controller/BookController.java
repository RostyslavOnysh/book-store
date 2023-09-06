package spring.boot.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import spring.boot.bookstore.dto.BookSearchParameter;
import spring.boot.bookstore.dto.request.CreateBookRequestDto;
import spring.boot.bookstore.dto.response.BookDto;
import spring.boot.bookstore.service.BookService;

@Tag(name = "Library management", description = "Endpoints for managing Library products")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books from the library",
            description = "Retrieves a paginated list of all available books in the library."
    )
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by its ID",
            description = "Retrieves a book from the library by its unique identifier (ID)."
    )
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new book",
            description = "Adds a new book to the library's collection."
                    + " Requires a valid book data payload in the request body.")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }

    @GetMapping("/title")
    @Operation(summary = "Get books by title",
            description = "Retrieves a list of books in the library that match the specified title."
    )
    public List<BookDto> getAllByTitle(@RequestParam String title) {
        return bookService.getBookByTitle(title);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by its ID",
            description = "Soft deletes a book from the library by its unique identifier (ID)."
    )
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book by its ID",
            description = "Updates an existing book in the library with new"
                    + " information based on its unique identifier (ID)."
    )
    public BookDto updateById(@PathVariable Long id,
                              @RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.updateBook(id, bookRequestDto);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for books",
            description = "Searches for books in the library based on "
                    + "various search parameters such as title, author, or genre."
    )
    public List<BookDto> searchBooks(BookSearchParameter searchParameters) {
        return bookService.searchBooks(searchParameters);
    }
}
