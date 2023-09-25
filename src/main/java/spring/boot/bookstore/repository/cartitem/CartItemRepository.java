package spring.boot.bookstore.repository.cartitem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.boot.bookstore.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.shoppingCart.id =:shoppingCartId")
    Page<CartItem> findCartItemByShoppingCartId(Long shoppingCart, Pageable pageable);
}
