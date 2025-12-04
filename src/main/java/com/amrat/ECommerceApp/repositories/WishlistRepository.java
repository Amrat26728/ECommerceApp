package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}
