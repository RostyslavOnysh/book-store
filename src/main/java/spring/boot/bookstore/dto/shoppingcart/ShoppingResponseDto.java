package spring.boot.bookstore.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
