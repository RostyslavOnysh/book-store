package spring.boot.bookstore.service.shoppingcart.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.model.ShoppingCart;
import spring.boot.bookstore.model.User;
import spring.boot.bookstore.repository.shoppingcart.ShoppingCartRepository;

@Service
@RequiredArgsConstructor
public class ShoppingCartManager {
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart registerNewCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }
}
