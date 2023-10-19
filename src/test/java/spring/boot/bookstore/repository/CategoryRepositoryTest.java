package spring.boot.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import spring.boot.bookstore.model.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    private static final String VALID_CATEGORY = "Fantasy";
    private static final String VALID_CATEGORY_2 = "Science Fiction";
    private static final String INVALID_CATEGORY = "Non-Existent Category";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Category category1 = new Category();
        category1.setName(VALID_CATEGORY);
        entityManager.persistAndFlush(category1);

        Category category2 = new Category();
        category2.setName(VALID_CATEGORY_2);
        entityManager.persistAndFlush(category2);
    }

    @Test
    @DisplayName("Find category by name")
    void findCategoryByName() {
        Category fantasyCategory = categoryRepository.findByName(VALID_CATEGORY);
        assertNotNull(fantasyCategory);
        assertEquals(VALID_CATEGORY, fantasyCategory.getName());
    }

    @Test
    @DisplayName("Find non-existent category by name")
    void findNonExistentCategoryByName() {
        Category nonExistentCategory = categoryRepository.findByName(INVALID_CATEGORY);
        assertNull(nonExistentCategory);
    }
}
