package spring.boot.bookstore.service.shoppingcart.impl;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.shoppingcart.ShoppingResponseDto;
import spring.boot.bookstore.mapper.CartItemMapper;
import spring.boot.bookstore.model.CartItem;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.cartitem.CartItemRepository;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;
import spring.boot.bookstore.service.cartitem.CartItemService;
import spring.boot.bookstore.service.shoppingcart.ShoppingCartService;
import spring.boot.bookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemResponseDto save(CartItemRequestDto requestDto) {
        return cartItemService.save(requestDto);
    }

    @Override
    public ShoppingCart getShoppingCart() {
        User authUser = userService.getAuthenticated();
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(authUser.getId());
        return shoppingCart.orElse(null);
    }

    @Override
    public ShoppingResponseDto removeItemFromShoppingCart(Long shoppingCartId, Long bookId) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (shoppingCart.isPresent()) {
            Optional<CartItem> cartItemToRemove = shoppingCart.get().getCartItems().stream()
                    .filter(cartItem -> cartItem.getBook().getId().equals(bookId))
                    .findFirst();
            if (cartItemToRemove.isPresent()) {
                shoppingCart.get().getCartItems().remove(cartItemToRemove.get());
                cartItemRepository.deleteById(cartItemToRemove.get().getId());
                ShoppingResponseDto responseDto = new ShoppingResponseDto();
                responseDto.setId(shoppingCart.get().getId());
                responseDto.setUserId(shoppingCart.get().getUser().getId());
                responseDto.setCartItems(shoppingCart.get().getCartItems().stream()
                        .map(cartItemMapper::toDto)
                        .collect(Collectors.toSet()));
                return responseDto;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
