package spring.boot.bookstore.service.cartitem;

import java.util.Set;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.cartitem.UpdateQuantityDto;
import spring.boot.bookstore.model.CartItem;
import spring.boot.bookstore.model.User;

public interface CartItemService {

    CartItemResponseDto save(CartItemRequestDto cartItemRequestDto);

    Set<CartItemResponseDto> findByShoppingCartId(Long id);

    CartItemResponseDto update(UpdateQuantityDto updateQuantityDto, Long id);

    void setShoppingCartAndCartItems(User user, CartItem cartItem);

    void delete(Long cartItemId);
}
