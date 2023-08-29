package spring.boot.bookstore.repository;

import java.util.List;
import java.util.Optional;
import spring.boot.bookstore.model.Book;

public interface BookRepository {
    Book createBook(Book book);

    Optional<Book> getBookById(Long id);

    List<Book> getAll();
}
