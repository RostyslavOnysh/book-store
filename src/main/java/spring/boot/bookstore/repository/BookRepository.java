package spring.boot.bookstore.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.boot.bookstore.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByTitleContainsIgnoreCase(String title);

    @Query("FROM Book b JOIN b.categories c WHERE c.id =:categoryId")
    List<Book> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("FROM Book b INNER JOIN FETCH b.categories")
    List<Book> findAllWithCategories(Pageable peageable);

}
