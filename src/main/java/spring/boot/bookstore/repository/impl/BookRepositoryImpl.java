package spring.boot.bookstore.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import spring.boot.bookstore.exception.DataProcessingException;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.repository.BookRepository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory entityManagerFactory;

    public BookRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Book createBook(Book book) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert a book: " + book, e);
        }
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Book book = entityManager.find(Book.class, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't find books by id: " + id);
        }
    }

    @Override
    public List<Book> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT b FROM Book b", Book.class).getResultList();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't find all books");
        }
    }
}
