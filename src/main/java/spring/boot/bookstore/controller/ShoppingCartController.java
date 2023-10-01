package spring.boot.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.cartitem.UpdateQuantityDto;
import spring.boot.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import spring.boot.bookstore.service.cartitem.CartItemService;
import spring.boot.bookstore.service.shoppingcart.ShoppingCartService;

@Tag(name = "Shopping Cart Controller management",
        description = "Endpoints for managing Library products in Shopping Carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @PostMapping
    @Operation(summary = "add new item to a shopping cart")
    public CartItemResponseDto addCartItem(@RequestBody @Valid
                                               CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.save(cartItemRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get shopping cart")
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update a cart item by ID")
    public CartItemResponseDto update(@RequestBody @Valid UpdateQuantityDto updateQuantityDto,
                                      @PathVariable Long id) {
        return cartItemService.update(updateQuantityDto, id);
    }

    @DeleteMapping("/{cartItemId}")
    @Operation(summary = "Delete a cart item by ID")
    public void deleteCartItemById(@PathVariable Long cartItemId) {
        cartItemService.delete(cartItemId);
    }
}
