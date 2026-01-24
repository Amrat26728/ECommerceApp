package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUser(User user);
}
