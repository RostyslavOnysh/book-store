package spring.boot.bookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.boot.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByTitleContainsIgnoreCase(String title);

    @Query("SELECT b FROM Book b WHERE "
            + "(:title is null or lower(b.title) like %:title%) "
            + "and (:author is null or lower(b.author) like %:author%) "
            + "and (:isbn is null or b.isbn = :isbn)")
    List<Book> searchBooks(@Param("title") String title,
                           @Param("author") String author,
                           @Param("isbn") String isbn);

}
