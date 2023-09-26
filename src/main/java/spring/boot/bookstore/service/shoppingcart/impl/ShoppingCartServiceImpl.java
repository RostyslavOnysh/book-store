package spring.boot.bookstore.service.shoppingcart.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
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
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getShoppingCart() {
        User authenticatedUser = userService.getAuthenticated();
        ShoppingCart shoppingCart = shoppingCartRepository
                .findById(authenticatedUser.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find a shopping cart with ID : "
                        + authenticatedUser.getId()));
        Long id = shoppingCart.getId();
        ShoppingCartResponseDto shoppingCartDto = new ShoppingCartResponseDto();
        shoppingCartDto.setId(id);
        shoppingCartDto.setUserId(authenticatedUser.getId());
        shoppingCartDto.setCartItems(cartItemService.findByShoppingCartId(id));
        return shoppingCartDto;
    }
}
