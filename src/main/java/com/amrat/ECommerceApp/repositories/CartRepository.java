package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
