package spring.boot.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.boot.bookstore.model.Order;
import spring.boot.bookstore.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryIntegrationTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setShippingAddress("123 Main St");
        user = userRepository.save(user);
        order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.COMPLETED);
        order.setTotal(BigDecimal.valueOf(100.0));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress("123 Main St");
        orderRepository.save(order);
    }

    @Test
    @DisplayName("Find all orders for a user with id ")
    void findAllOrders() {
        List<Order> actualOrders = orderRepository.findAllOrders(1L);
        assertEquals(2, actualOrders.size());
    }

    @Test
    @DisplayName("Find order with order items by ID")
    void findOrderWithOrderItems() {
        Optional<Order> actualOrder = orderRepository.findById(1L);
        assertTrue(actualOrder.isPresent());
    }

    @Test
    @DisplayName("Save Order and Verify Its ID")
    void saveOrder() {
        Order savedOrder = orderRepository.save(order);
        assertNotNull(savedOrder.getId());
        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());
        assertTrue(retrievedOrder.isPresent());
    }

    @Test
    @DisplayName("Delete Order and Ensure It's Removed")
    void deleteOrder() {
        Order savedOrder = orderRepository.save(order);
        orderRepository.delete(savedOrder);
        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());
        assertTrue(retrievedOrder.isEmpty());
    }

    @Test
    @DisplayName("Find Orders by User")
    void findOrdersByUser() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);
        List<Order> orders = orderRepository.findAllOrders(user.getId());
        assertEquals(2, orders.size());
    }

    @Test
    @DisplayName("Update Order Status")
    void updateOrder() {
        Order savedOrder = orderRepository.save(order);
        savedOrder.setStatus(Order.Status.PENDING);
        Order updatedOrder = orderRepository.save(savedOrder);
        assertEquals(Order.Status.PENDING, updatedOrder.getStatus());
    }
}
