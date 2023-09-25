package spring.boot.bookstore.service.cartitem;

import java.util.Set;
import org.springframework.data.domain.Pageable;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.cartitem.UpdateQuantityDto;

public interface CartItemService {

    CartItemResponseDto save(CartItemRequestDto cartItemRequestDto);

    Set<CartItemResponseDto> getCartItemById(Long id);

    CartItemResponseDto update(UpdateQuantityDto updateQuantityDto, Long id);

    CartItemResponseDto getAllCartItemsForShoppingCart(Long shoppingCartId, Pageable pageable);

    void delete(Long id);
}
