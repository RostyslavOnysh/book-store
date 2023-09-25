package spring.boot.bookstore.service.cartitem.impl;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.cartitem.UpdateQuantityDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.mapper.CartItemMapper;
import spring.boot.bookstore.model.CartItem;
import spring.boot.bookstore.repository.BookRepository;
import spring.boot.bookstore.repository.cartitem.CartItemRepository;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;
import spring.boot.bookstore.service.cartitem.CartItemService;
import spring.boot.bookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemMapper mapper;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Override
    public CartItemResponseDto save(CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(bookRepository.getById(cartItemRequestDto.getBookId()));
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        Long id = userService.getAuthenticated().getId();
        cartItem.setShoppingCart(shoppingCartRepository.getById(id));
        return mapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public Set<CartItemResponseDto> getCartItemById(Long id) {
        return cartItemRepository.findById(id).stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public CartItemResponseDto update(UpdateQuantityDto updateQuantityDto, Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find Entity by ID" + id));
        cartItem.setQuantity(updateQuantityDto.getQuantity());
        return mapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponseDto getAllCartItemsForShoppingCart(Long shoppingCartId,
                                                              Pageable pageable) {
        Set<CartItemResponseDto> cartItemResponseDto =
                cartItemRepository.findCartItemByShoppingCartId(shoppingCartId, pageable)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet());
        if (cartItemResponseDto.isEmpty()) {
            return null;
        } else {
            return cartItemResponseDto.iterator().next();
        }
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}

