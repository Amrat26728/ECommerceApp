package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
}
