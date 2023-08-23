package spring.boot.bookstore.service;

import spring.boot.bookstore.model.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
