package spring.boot.bookstore.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.bookstore.dto.order.OrderRequestDto;
import spring.boot.bookstore.dto.order.OrderResponseDto;
import spring.boot.bookstore.dto.orderitem.OrderItemResponseDto;
import spring.boot.bookstore.exception.EntityNotFoundException;
import spring.boot.bookstore.mapper.OrderItemMapper;
import spring.boot.bookstore.mapper.OrderMapper;
import spring.boot.bookstore.model.Order;
import spring.boot.bookstore.model.OrderItem;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.OrderRepository;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;
import spring.boot.bookstore.service.emailsender.EmailServiceImpl;
import spring.boot.bookstore.service.shoppingcart.impl.ShoppingCartManager;
import spring.boot.bookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository repository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ShoppingCartManager registerNewCart;
    private final EmailServiceImpl emailService;

    @Override public OrderResponseDto create(Long id, OrderRequestDto orderRequestDto) {
        User authUser = userService.getAuthenticated();
        ShoppingCart shoppingCart = shoppingCartRepository.getUserById(authUser.getId())
                .orElseGet(() -> registerNewCart.registerNewCart(authUser));
        Order order = new Order();
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setUser(shoppingCart.getUser());
        order.setTotal(BigDecimal.ZERO);
        order.setStatus(Order.Status.PENDING);
        Set<OrderItem> orderItems = getOrderItemsFromShoppingCart(shoppingCart, order);
        order.setOrderItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        BigDecimal totalPrice = orderItems
                .stream()
                .map(orderItem -> orderItem
                        .getBook()
                        .getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(totalPrice);
        return orderMapper.toDto(repository.save(order));
    }

    @Override
    public List<OrderResponseDto> findAllOrders(Long id, Pageable pageable) {
        return repository.findAllOrders(id).stream().map(orderMapper::toDto).toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, Order.Status status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        order = repository.save(order);
        emailService.sendStatusChangeEmail(order.getUser().getEmail(), status);
        return orderMapper.toDto(order);
    }

    private Order getOrderById(Long orderId) {
        return repository.findById(orderId).orElseThrow(() ->
                        new EntityNotFoundException("can't find order by id: " + orderId));
    }

    @Override
    @Transactional
    public Set<OrderItemResponseDto> findAllOrderItems(Long orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("cant find Order using provided ID : "
                                + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    private Set<OrderItem> getOrderItemsFromShoppingCart(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(order);
                    orderItem.setPrice(cartItem.getBook().getPrice()
                            .multiply(new BigDecimal(cartItem.getQuantity())));
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemResponseDto findOrderItemById(Long orderId, Long itemId) {
        Order order = repository.findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("cant find order by provided ID : "
                                + orderId));
        return order.getOrderItems()
                .stream()
                .filter(o -> o.getId().equals(itemId))
                .findFirst()
                .map(orderItemMapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("cant find item using provided ID : "
                                + itemId
                                + ", with order id"
                                + orderId));
    }
}
