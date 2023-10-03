package spring.boot.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.bookstore.dto.order.OrderRequestDto;
import spring.boot.bookstore.dto.order.OrderResponseDto;
import spring.boot.bookstore.dto.order.OrderUpdateDto;
import spring.boot.bookstore.dto.orderitem.OrderItemResponseDto;
import spring.boot.bookstore.model.Order;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.service.order.OrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller management", description = "Endpoints for managing users orders")
public class OrderController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place order", description = "place order")
    public OrderResponseDto create(Authentication authentication,
                                   @RequestBody
                                   @Valid OrderRequestDto orderRequestDto) {
        User user = (User) authentication.getPrincipal();
        logger.info("Placing a new Order.");
        return orderService.create(user.getId(), orderRequestDto);
    }

    @Operation(summary = "Get all users order history", description = "Get all users order history")
    @GetMapping
    public List<OrderResponseDto> findAll(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        logger.info("Find all  Orders.");
        return orderService.findAllOrders(user.getId(), pageable);
    }

    @Operation(summary = "update order", description = "update order status")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                              @RequestBody OrderUpdateDto orderUpdateDto) {
        logger.info("updating Order Status by id." + id);
        return orderService.updateOrderStatus(id, Order.Status
                .valueOf(String.valueOf(orderUpdateDto.getStatus())));
    }

    @Operation(summary = "", description = "")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public Set<OrderItemResponseDto> findAllOrderItems(@PathVariable Long orderId) {
        logger.info("find All Order Items using id" + orderId);
        return orderService.findAllOrderItems(orderId);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto findOrderItemById(@PathVariable Long orderId,
                                                  @PathVariable Long itemId) {
        logger.info("find Order Item By Id" + orderId + "and here Item id : " + itemId);
        return orderService.findOrderItemById(orderId, itemId);
    }
}
