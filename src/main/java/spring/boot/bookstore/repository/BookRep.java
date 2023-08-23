package spring.boot.bookstore.repository;

import spring.boot.bookstore.model.Book;

import java.util.List;

public interface BookRep {

    Book save (Book book);

    List<Book> findAll();
}
