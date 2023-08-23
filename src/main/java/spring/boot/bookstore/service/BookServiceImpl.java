package spring.boot.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.repository.BookRep;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRep bookRep;

    @Autowired
    public BookServiceImpl(BookRep bookRep) {
        this.bookRep = bookRep;
    }

    @Override
    public Book save(Book book) {
        return bookRep.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRep.findAll();
    }
}
