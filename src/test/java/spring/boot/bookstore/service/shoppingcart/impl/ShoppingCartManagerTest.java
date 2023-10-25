package spring.boot.bookstore.service.shoppingcart.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class ShoppingCartManagerTest {
    @InjectMocks
    private ShoppingCartManager shoppingCartManager;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Bob");
        user.setLastName("Bobovchenko");
        user.setEmail("newBobMail@gmail.com");
        user.setPassword("BobDon`tLikeMakeNewPAssword_%^&*4567");
    }

    @Test
    void testRegisterNewCartWithValidUser() {
        ShoppingCart newCart = shoppingCartManager.registerNewCart(user);
        Long cartId = newCart.getId();
        assertEquals(newCart.getUser(), user);
        assertEquals(newCart.getId(), cartId);
    }

    @Test
    void testRegisterNewCartWithRepositorySave() {
        ShoppingCart newCart = shoppingCartManager.registerNewCart(user);
        Long cartId = newCart.getId();
        assertEquals(newCart.getUser(), user);
        assertEquals(newCart.getId(), cartId);
        verify(shoppingCartRepository).save(newCart);
    }
}
