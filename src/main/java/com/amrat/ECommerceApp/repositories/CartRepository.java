package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Cart;
import com.amrat.ECommerceApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
