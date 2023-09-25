package spring.boot.bookstore.mapper;

import org.mapstruct.Mapper;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.shoppingcart.ShoppingResponseDto;
import spring.boot.bookstore.model.ShoppingCart;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingResponseDto toDto(ShoppingCart shoppingCart);
}
