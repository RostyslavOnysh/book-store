package spring.boot.bookstore.service.shoppingcart.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;
import spring.boot.bookstore.service.cartitem.CartItemService;
import spring.boot.bookstore.service.user.UserService;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class ShoppingCartServiceImplTest {
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private UserService userService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Bob");
        user.setLastName("Alison");
        user.setEmail("bob@gmail.com");
        user.setPassword("newBobPassword78%&*(_");
    }

    @Test
    @DisplayName("save shopping cart")
    void save() {
        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setBookId(1L);
        requestDto.setQuantity(4);
        when(cartItemService.save(requestDto)).thenReturn(new CartItemResponseDto());
        CartItemResponseDto response = shoppingCartService.save(requestDto);
        assertNotNull(response);
    }

    @Test
    @DisplayName("get Shopping Cart When User Authenticated")
    void getShoppingCartWhenUserAuthenticated() {
        when(userService.getAuthenticated()).thenReturn(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(2L);
        when(shoppingCartRepository.getUserById(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        ShoppingCartResponseDto response = shoppingCartService.getShoppingCart();
        assertNotNull(response);
        assertEquals(2L, response.getId());
    }

    @Test
    @DisplayName("get Shopping Cart")
    void getShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        when(userService.getAuthenticated()).thenReturn(user);
        when(shoppingCartRepository.getUserById(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        ShoppingCartResponseDto response = shoppingCartService.getShoppingCart();
        assertNotNull(response);
    }
}
