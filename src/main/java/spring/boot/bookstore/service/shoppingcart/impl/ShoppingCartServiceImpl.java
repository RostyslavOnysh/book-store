package spring.boot.bookstore.service.shoppingcart.impl;

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

import java.util.Optional;
import java.util.stream.Collectors;

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
        /*
        User authUser = userService.getAuthenticated();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(authUser.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Can`t find shopping cart by id : "
                        + authUser.getId()));
        Long id = shoppingCart.getId();
        ShoppingResponseDto responseDto = new ShoppingResponseDto();
        responseDto.setId(id);
        responseDto.setUserId(authUser.getId());
        responseDto.setCartItems(cartItemService.getCartItemById(id));
        return responseDto;


        User authUser = userService.getAuthenticated();
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(authUser.getId());
        if (shoppingCart.isPresent()) {
            // Shopping cart exists
            Long id = shoppingCart.get().getId();
            ShoppingResponseDto responseDto = new ShoppingResponseDto();
            responseDto.setId(id);
            responseDto.setUserId(authUser.getId());
            responseDto.setCartItems(cartItemService.getCartItemById(id));
            return responseDto;
        } else {
            // Shopping cart does not exist
            return null;
        }

         */
        User authUser = userService.getAuthenticated();
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(authUser.getId());
        return shoppingCart.orElse(null);
    }

    @Override
    public ShoppingResponseDto removeItemFromShoppingCart(Long shoppingCartId, Long bookId) {
        /*
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));
        CartItem cartItemToRemove = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Book not found in the shopping cart"));
        shoppingCart.getCartItems().remove(cartItemToRemove);
        cartItemRepository.deleteById(cartItemToRemove.getId());
        ShoppingResponseDto responseDto = new ShoppingResponseDto();
        responseDto.setId(shoppingCart.getId());
        responseDto.setUserId(shoppingCart.getUser().getId());
        responseDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
        return responseDto;


        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (shoppingCart.isPresent()) {
            // Shopping cart exists
            Optional<CartItem> cartItemToRemove = shoppingCart.get().getCartItems().stream()
                    .filter(cartItem -> cartItem.getBook().getId().equals(bookId))
                    .findFirst();
            if (cartItemToRemove.isPresent()) {
                // Cart item exists
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
                // Cart item does not exist
                return null;
            }
        } else {
            // Shopping cart does not exist
            return null;
        }

         */
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (shoppingCart.isPresent()) {
            Optional<CartItem> cartItemToRemove = shoppingCart.get().getCartItems().stream()
                    .filter(cartItem -> cartItem.getBook().getId().equals(bookId))
                    .findFirst();
            if (cartItemToRemove.isPresent()) {
                // Cart item exists
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
                // Cart item does not exist
                return null;
            }
        } else {
            // Shopping cart does not exist
            return null;
        }

    }
}
