package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
