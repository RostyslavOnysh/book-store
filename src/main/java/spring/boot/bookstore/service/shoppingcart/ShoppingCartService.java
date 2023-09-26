package spring.boot.bookstore.service.shoppingcart;

import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    CartItemResponseDto save(CartItemRequestDto requestDto);

    ShoppingCartResponseDto getShoppingCart();
}
