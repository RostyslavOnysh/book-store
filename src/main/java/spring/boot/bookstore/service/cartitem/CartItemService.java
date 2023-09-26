package spring.boot.bookstore.service.cartitem;

import java.util.Set;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.cartitem.UpdateQuantityDto;
import spring.boot.bookstore.model.CartItem;

public interface CartItemService {

    CartItemResponseDto save(CartItemRequestDto cartItemRequestDto);

    Set<CartItemResponseDto> findByShoppingCartId(Long id);

    CartItemResponseDto update(UpdateQuantityDto updateQuantityDto, Long id);

    void setShoppingCartAndCartItems(Long id, CartItem cartItem);

    void delete(Long id);
}
