package spring.boot.bookstore.service.order;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import spring.boot.bookstore.dto.order.OrderRequestDto;
import spring.boot.bookstore.dto.order.OrderResponseDto;
import spring.boot.bookstore.dto.orderitem.OrderItemResponseDto;
import spring.boot.bookstore.model.Order;

public interface OrderService {
    OrderResponseDto create(Long id, OrderRequestDto orderRequestDto);

    List<OrderResponseDto> findAllOrders(Long id, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId, Order.Status status);

    Set<OrderItemResponseDto> findAllOrderItems(Long orderId);

    OrderItemResponseDto findOrderItemById(Long orderId, Long itemId);
}
