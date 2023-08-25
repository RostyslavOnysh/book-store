package spring.boot.bookstore.repository;

import java.util.List;
import spring.boot.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
