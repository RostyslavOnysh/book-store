package spring.boot.bookstore.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.repository.BookRepository;
import spring.boot.bookstore.service.BookService;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
