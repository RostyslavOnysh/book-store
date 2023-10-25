package spring.boot.bookstore.repository.cartitem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.model.CartItem;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.BookRepository;
import spring.boot.bookstore.repository.UserRepository;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Bob");
        user.setLastName("Alison");
        user.setEmail("example@example.com");
        user.setPassword("password");
        userRepository.save(user);
    }

    @Test
    @DisplayName("find Cart Item By ShoppingCart Id")
    void findCartItemByShoppingCartId() {
        ShoppingCart savedShoppingCart = new ShoppingCart();
        savedShoppingCart.setUser(user);
        savedShoppingCart = shoppingCartRepository.save(savedShoppingCart);
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor("Sample Author");
        book.setIsbn("5678");
        book.setPrice(BigDecimal.valueOf(29.99));
        book = bookRepository.save(book);
        CartItem cartItem1 = new CartItem();
        cartItem1.setShoppingCart(savedShoppingCart);
        cartItem1.setBook(book);
        cartItemRepository.save(cartItem1);
        Set<CartItem> result = cartItemRepository
                .findCartItemByShoppingCartId(savedShoppingCart.getId());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("find Cart Items By Shopping CartId Expect No Result")
    void findCartItemsByShoppingCartId_ExpectNoResult() {
        ShoppingCart savedShoppingCart = new ShoppingCart();
        savedShoppingCart.setUser(user);
        Long nonExistentShoppingCartId = 999L;
        Set<CartItem> result = cartItemRepository
                .findCartItemByShoppingCartId(nonExistentShoppingCartId);
        assertEquals(0, result.size());
    }
}
