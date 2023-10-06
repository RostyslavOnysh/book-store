package spring.boot.bookstore.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.boot.bookstore.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT  o FROM Order o LEFT JOIN FETCH o.orderItems"
            + " LEFT JOIN FETCH o.user u WHERE u.id = :userId")
    List<Order> findAllOrders(long userId);

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findById(Long id);
}
