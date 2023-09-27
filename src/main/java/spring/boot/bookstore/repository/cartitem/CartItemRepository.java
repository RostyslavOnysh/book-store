package spring.boot.bookstore.repository.cartitem;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.boot.bookstore.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.shoppingCart.id =:shoppingCartId")
    Set<CartItem> findCartItemByShoppingCartId(@Param("shoppingCartId") Long shoppingCart);
}
