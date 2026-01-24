package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Cart;
import com.amrat.ECommerceApp.entities.CartItem;
import com.amrat.ECommerceApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProduct(Cart cart, Product product);

    int deleteByCartAndProduct(Cart cart, Product product);
}
