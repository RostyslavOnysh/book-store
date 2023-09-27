package spring.boot.bookstore.service.shoppingcart.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;
import spring.boot.bookstore.service.cartitem.CartItemService;
import spring.boot.bookstore.service.shoppingcart.ShoppingCartService;
import spring.boot.bookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartItemService cartItemService;
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartItemResponseDto save(CartItemRequestDto requestDto) {
        return cartItemService.save(requestDto);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCart() {
        User authenticatedUser = userService.getAuthenticated();
        ShoppingCart shoppingCart = shoppingCartRepository
                .findUserById(authenticatedUser.getId()) // change to findUserById
                .orElseGet(() -> registerNewCart(authenticatedUser));
        Long id = shoppingCart.getId();
        ShoppingCartResponseDto shoppingCartDto = new ShoppingCartResponseDto();
        shoppingCartDto.setId(id);
        shoppingCartDto.setUserId(authenticatedUser.getId());
        shoppingCartDto.setCartItems(cartItemService.findByShoppingCartId(id));
        return shoppingCartDto;
    }

    private ShoppingCart registerNewCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }
}
