package spring.boot.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.orderitem.OrderItemResponseDto;
import spring.boot.bookstore.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mappings({@Mapping(target = "bookId", source = "book.id")})
    OrderItemResponseDto toDto(OrderItem orderItem);
}
