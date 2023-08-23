package spring.boot.bookstore.service;

import java.util.List;
import spring.boot.bookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
