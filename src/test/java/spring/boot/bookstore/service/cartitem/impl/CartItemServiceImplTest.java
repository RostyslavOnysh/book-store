package spring.boot.bookstore.service.cartitem.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
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
import spring.boot.bookstore.dto.cartitem.UpdateQuantityDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.mapper.CartItemMapper;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.model.CartItem;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.BookRepository;
import spring.boot.bookstore.repository.cartitem.CartItemRepository;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;
import spring.boot.bookstore.service.user.UserService;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class CartItemServiceImplTest {
    private static final Long INVALID_ID = -1L;
    private static final Long VALID_SHOPPING_CART_ID = 1L;
    private static final Book VALID_BOOK = new Book();
    private static final User VALID_USER = new User();
    private static final ShoppingCart VALID_SHOPPING_CART = new ShoppingCart();
    private static final CartItemRequestDto VALID_REQUEST = new CartItemRequestDto();
    private static final CartItemResponseDto VALID_RESPONSE_DTO = new CartItemResponseDto();
    private static final CartItem VALID_CART_ITEM = new CartItem();
    private static final UpdateQuantityDto VALID_UPDATE_REQUEST = new UpdateQuantityDto();

    @InjectMocks
    private CartItemServiceImpl cartItemService;
    @Mock
    private UserService userService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    void setUp() {
        VALID_BOOK.setId(1L);
        VALID_BOOK.setTitle("Viy");
        VALID_BOOK.setAuthor("Nikolai Gogol");
        VALID_BOOK.setIsbn("66-666-66");
        VALID_BOOK.setPrice(new BigDecimal("689.00"));
        VALID_BOOK.setDescription("is a horror novella by the writer Nikolai Gogol,"
                + " first published in volume 2 of his collection of "
                + "tales entitled Mirgorod (1835).");

        VALID_USER.setId(1L);
        VALID_USER.setFirstName("Bob");
        VALID_USER.setLastName("Alison");
        VALID_USER.setEmail("bobEmail@i.ua");
        VALID_USER.setPassword("password");

        VALID_SHOPPING_CART.setId(1L);
        VALID_SHOPPING_CART.setUser(VALID_USER);
        VALID_SHOPPING_CART.setCartItems(new ArrayList<>());

        VALID_REQUEST.setBookId(VALID_BOOK.getId());
        VALID_REQUEST.setQuantity(2);

        VALID_CART_ITEM.setId(1L);
        VALID_CART_ITEM.setBook(VALID_BOOK);
        VALID_CART_ITEM.setQuantity(VALID_REQUEST.getQuantity());
        VALID_CART_ITEM.setShoppingCart(VALID_SHOPPING_CART);

        VALID_RESPONSE_DTO.setId(VALID_CART_ITEM.getId());
        VALID_RESPONSE_DTO.setBookId(VALID_CART_ITEM.getId());
        VALID_RESPONSE_DTO.setBookTitle(VALID_CART_ITEM.getBook().getTitle());
        VALID_RESPONSE_DTO.setQuantity(VALID_CART_ITEM.getQuantity());

        VALID_UPDATE_REQUEST.setQuantity(100);
    }

    @Test
    @DisplayName("save cartItem and return correct cart item response")
    void saveCartItem_thenReturnCartItemResponseDto() {
        when(userService.getAuthenticated()).thenReturn(VALID_USER);
        when(bookRepository.getById(VALID_REQUEST.getBookId())).thenReturn(VALID_BOOK);
        when(shoppingCartRepository.getUserById(VALID_USER.getId()))
                .thenReturn(Optional.of(VALID_SHOPPING_CART));
        when(cartItemMapper.toDto(cartItemRepository.save(VALID_CART_ITEM)))
                .thenReturn(VALID_RESPONSE_DTO);

        CartItemResponseDto cartItemResponseDto = cartItemService.save(VALID_REQUEST);

        assertEquals(VALID_RESPONSE_DTO, cartItemResponseDto);
    }

    @Test
    @DisplayName("update cart item quantity and then return correct cart item response ")
    void updateCartItemQuantity_thenReturnCartItemResponseDto() {
        when(cartItemRepository.findById(VALID_CART_ITEM.getId()))
                .thenReturn(Optional.of(VALID_CART_ITEM));
        when(cartItemMapper.toDto(cartItemRepository.save(VALID_CART_ITEM)))
                .thenReturn(VALID_RESPONSE_DTO);
        CartItemResponseDto cartItemResponseDto = cartItemService
                .update(VALID_UPDATE_REQUEST, VALID_CART_ITEM.getId());
        assertEquals(VALID_RESPONSE_DTO, cartItemResponseDto);
    }

    @Test
    @DisplayName("find by shopping cart id using valid id and return item ")
    void findByShoppingCartId_validId_returnOneItem() {
        when(cartItemRepository.findCartItemByShoppingCartId(anyLong()))
                .thenReturn(Set.of(VALID_CART_ITEM));
        when(cartItemMapper.toDto(any())).thenReturn(VALID_RESPONSE_DTO);

        Set<CartItemResponseDto> actual = cartItemService
                .findByShoppingCartId(VALID_SHOPPING_CART_ID);
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(Set.of(VALID_RESPONSE_DTO), actual);
        verify(cartItemRepository, times(1))
                .findCartItemByShoppingCartId(anyLong());
        verify(cartItemMapper, times(1)).toDto(any());
        verifyNoMoreInteractions(cartItemMapper, cartItemRepository);
    }

    @Test
    @DisplayName("Verify update() method")
    void update_validUpdateRequest_returnResponse() {
        when(cartItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(VALID_CART_ITEM));
        when(cartItemRepository.save(any())).thenReturn(VALID_CART_ITEM);
        when(cartItemMapper.toDto(any())).thenReturn(VALID_RESPONSE_DTO);
        CartItemResponseDto actual = cartItemService.update(VALID_UPDATE_REQUEST, 1L);
        VALID_RESPONSE_DTO.setQuantity(VALID_UPDATE_REQUEST.getQuantity());
        assertNotNull(actual);
        assertEquals(VALID_RESPONSE_DTO, actual);
        verify(cartItemRepository, times(1)).findById(anyLong());
        verify(cartItemRepository, times(1)).save(any());
        verify(cartItemMapper, times(1)).toDto(any());
        verifyNoMoreInteractions(cartItemMapper, cartItemRepository);
    }

    @Test
    @DisplayName("Verify update() method with non-existent cart item")
    void update_nonExistentCartItem_throwException() {
        assertThrows(EntityNotFoundException.class,
                () -> cartItemService.update(VALID_UPDATE_REQUEST, INVALID_ID));
    }

    @Test
    @DisplayName("Verify delete() method with non-existent cart item")
    void delete_nonExistentCartItem_throwException() {
        assertThrows(EntityNotFoundException.class,
                () -> cartItemService.delete(INVALID_ID));
    }
}
