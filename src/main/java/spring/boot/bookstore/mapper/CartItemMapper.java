package spring.boot.bookstore.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.cartitem.CartItemResponseDto;
import spring.boot.bookstore.model.CartItem;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    @AfterMapping
    default void setBookId(@MappingTarget CartItemResponseDto cartItemResponseDto,
                           CartItem cartItem) {
        cartItemResponseDto.setBookId(cartItem.getBook().getId());
    }

    @AfterMapping
    default void setBookTitle(@MappingTarget CartItemResponseDto cartItemResponseDto,
                              CartItem cartItem) {
        cartItemResponseDto.setBookTitle(String.valueOf(cartItem.getBook().getId()));
    }
}
