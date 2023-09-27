package spring.boot.bookstore.service.cartitem.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.cartitem.UpdateQuantityDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.mapper.CartItemMapper;
import spring.boot.bookstore.model.CartItem;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.BookRepository;
import spring.boot.bookstore.repository.cartitem.CartItemRepository;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;
import spring.boot.bookstore.service.cartitem.CartItemService;
import spring.boot.bookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final CartItemMapper mapper;

    @Override
    @Transactional
    public CartItemResponseDto save(CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(bookRepository.getById(cartItemRequestDto.getBookId()));
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        User user = userService.getAuthenticated();
        setShoppingCartAndCartItems(user, cartItem);
        return mapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public Set<CartItemResponseDto> findByShoppingCartId(Long id) {
        return cartItemRepository.findCartItemByShoppingCartId(id).stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public CartItemResponseDto update(UpdateQuantityDto updateQuantityDto, Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find cart by ID : " + id));
        cartItem.setQuantity(updateQuantityDto.getQuantity());
        return mapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find cart by ID : " + id));
        cartItemRepository.deleteById(id);
    }

    @Override
    public void setShoppingCartAndCartItems(User user, CartItem cartItem) {
        ShoppingCart shoppingCart = shoppingCartRepository.findUserById(user.getId())
                .orElseGet(() -> registerNewCart(user));
        cartItem.setShoppingCart(shoppingCart);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        if (shoppingCart.getCartItems().isEmpty()) {
            shoppingCart.setCartItems(cartItems);
        } else {
            shoppingCart.getCartItems().add(cartItem);
        }
    }

    private ShoppingCart registerNewCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }
}
