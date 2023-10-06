package spring.boot.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.order.OrderResponseDto;
import spring.boot.bookstore.model.Order;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mappings({@Mapping(target = "userId", source = "user.id"),
            @Mapping(source = "orderDate", target = "orderTime")})
    @Mapping(source = "total", target = "totalPrice")
    OrderResponseDto toDto(Order order);
}
