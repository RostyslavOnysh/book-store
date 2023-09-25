package spring.boot.bookstore.service.shoppingcart;

import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.shoppingcart.ShoppingResponseDto;
import spring.boot.bookstore.model.ShoppingCart;

public interface ShoppingCartService {

    CartItemResponseDto save(CartItemRequestDto requestDto);

    ShoppingCart getShoppingCart();

    ShoppingResponseDto removeItemFromShoppingCart(Long shoppingCartId, Long bookId);
}
