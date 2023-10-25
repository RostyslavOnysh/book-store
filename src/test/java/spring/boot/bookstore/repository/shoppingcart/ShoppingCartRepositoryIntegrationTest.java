package spring.boot.bookstore.repository.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import spring.boot.bookstore.model.CartItem;
import spring.boot.bookstore.model.ShoppingCart;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
class ShoppingCartRepositoryIntegrationTest {
    private static final Long VALID_ID = 1L;
    @MockBean
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItem cartItem;

    @Test
    @DisplayName("get user by id")
    void getUserById() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(VALID_ID);
        when(shoppingCartRepository.getUserById(any())).thenReturn(Optional.of(shoppingCart));
        Optional<ShoppingCart> result = shoppingCartRepository.getUserById(VALID_ID);
        assertEquals(shoppingCart, result.get());
    }

    @Test
    @DisplayName("get User By Id Not Found")
    void getUserById_NotFound() {
        when(shoppingCartRepository.getUserById(any())).thenReturn(Optional.empty());
        Optional<ShoppingCart> result = shoppingCartRepository.getUserById(VALID_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("find by id ")
    void findById() {
        ShoppingCart shoppingCart = new ShoppingCart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);
        when(shoppingCartRepository.findById(VALID_ID)).thenReturn(Optional.of(shoppingCart));
        Optional<ShoppingCart> result = shoppingCartRepository.findById(VALID_ID);
        assertEquals(shoppingCart, result.orElse(null));
    }

    @Test
    @DisplayName("find Non Existing ShoppingCart Expect Failure")
    void findNonExistingShoppingCart_ExpectFailure() {
        when(shoppingCartRepository.getUserById(any())).thenReturn(Optional.empty());
        Optional<ShoppingCart> result = shoppingCartRepository.getUserById(VALID_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("delete ShoppingCart")
    void deleteShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        Long savedId = shoppingCart.getId();
        shoppingCartRepository.delete(shoppingCart);
        Optional<ShoppingCart> deleteCart = shoppingCartRepository.getUserById(savedId);
        assertTrue(deleteCart.isEmpty());
    }
}
