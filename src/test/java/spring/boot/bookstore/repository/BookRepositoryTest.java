package spring.boot.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.model.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final String INVALID_TITLE = "Non-Existent Title";
    private static final Long INVALID_CATEGORY_ID = -1L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName("Fantasy");
        categoryRepository.save(category);
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("6788");
        book1.setPrice(BigDecimal.valueOf(20));
        book1.setDescription("Description 1");
        book1.setCoverImage("Image 1");
        book1.getCategories().add(category);
        bookRepository.save(book1);
        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("5678");
        book2.setPrice(BigDecimal.valueOf(30));
        book2.setDescription("Description 2");
        book2.setCoverImage("Image 2");
        book2.getCategories().add(category);
        bookRepository.save(book2);
    }

    @Test
    @DisplayName("Find book by title ignoring case")
    void findAllByTitleContainsIgnoreCase() {
        List<Book> actual = bookRepository
                .findAllByTitleContainsIgnoreCase("Harry Potter");
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Find books by category id")
    void findBookByCategoriesId() {
        Category category = categoryRepository.findByName("Fantasy");
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> booksByCategoryId = bookRepository
                .findBookByCategoriesId(category.getId(), pageable);
        assertEquals(2, booksByCategoryId.size());
    }

    @Test
    @DisplayName("Find all books with categories")
    void findAllWithCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> actual = bookRepository
                .findAllWithCategories(pageable);
        Assertions.assertThat(actual).isNotEmpty();
    }

    @Test
    @DisplayName("Find all books by invalid category id")
    void findAllByCategoriesId_invalidCategoryId_returnEmptyList() {
        List<Long> invalidCategoryIds = List.of(-1L, -2L);
        List<Book> actual = bookRepository
                .findBookByCategoriesId(invalidCategoryIds.get(0),
                        PageRequest.of(0, 10));
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Find books by category id with real data")
    void findBookByCategoriesIdWithRealData() {
        Category category = categoryRepository.findByName("Fantasy");
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> booksByCategoryId = bookRepository
                .findBookByCategoriesId(category.getId(), pageable);
        org.junit.jupiter.api.Assertions.assertFalse(booksByCategoryId.isEmpty());
    }

    @Test
    @DisplayName("Find all books with categories with pagination")
    void findAllWithCategoriesWithPagination() {
        Pageable pageable = PageRequest.of(1, 1);
        List<Book> actual = bookRepository.findAllWithCategories(pageable);
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Find books by non-existent title")
    void findAllByNonExistentTitle() {
        List<Book> actual = bookRepository
                .findAllByTitleContainsIgnoreCase(INVALID_TITLE);
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Find books by invalid category id")
    void findBookByCategoriesIdWithInvalidCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> actual = bookRepository
                .findBookByCategoriesId(INVALID_CATEGORY_ID, pageable);
        assertTrue(actual.isEmpty());
    }
}
