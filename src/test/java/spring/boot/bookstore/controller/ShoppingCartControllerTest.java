package spring.boot.bookstore.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring.boot.bookstore.controller.CategoryControllerTest.convertObjectToJsonString;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.boot.bookstore.dto.cartitem.CartItemRequestDto;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.dto.cartitem.UpdateQuantityDto;
import spring.boot.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import spring.boot.bookstore.service.cartitem.CartItemService;
import spring.boot.bookstore.service.shoppingcart.ShoppingCartService;

@ContextConfiguration(classes = {ShoppingCartControllerTest.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(ShoppingCartControllerTest.class)
class ShoppingCartControllerTest {
    @InjectMocks
    private ShoppingCartController shoppingCartController;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private CartItemService cartItemService;

    @Test
    @DisplayName("adding item to the shoppingCart")
    void addCartItemTest() throws Exception {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(3L);
        cartItemRequestDto.setQuantity(14);
        when(shoppingCartService.save(cartItemRequestDto)).thenReturn(new CartItemResponseDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(cartItemRequestDto));
        MockMvcBuilders.standaloneSetup(shoppingCartController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("get shopping cart")
    void getShoppingCartTest() throws Exception {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        when(shoppingCartService.getShoppingCart()).thenReturn(shoppingCartResponseDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cart");
        MockMvcBuilders.standaloneSetup(shoppingCartController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(convertObjectToJsonString(shoppingCartResponseDto)));
    }

    @Test
    @DisplayName("update quantity in the Shopping Cart")
    void updateCartItemTest() throws Exception {
        UpdateQuantityDto updateQuantityDto = new UpdateQuantityDto();
        updateQuantityDto.setQuantity(3);
        when(cartItemService.update(updateQuantityDto, 1L)).thenReturn(new CartItemResponseDto());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("/cart/cart-items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(updateQuantityDto));
        MockMvcBuilders.standaloneSetup(shoppingCartController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("delete cart item by ID")
    void deleteCartItemById() throws Exception {
        Long cartItemId = 1L;
        doNothing().when(this.cartItemService).delete(cartItemId);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("/cart/" + cartItemId);
        MockMvcBuilders.standaloneSetup(this.shoppingCartController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());

    }
}
