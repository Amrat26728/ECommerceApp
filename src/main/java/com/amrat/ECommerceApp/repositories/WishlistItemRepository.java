package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.Wishlist;
import com.amrat.ECommerceApp.entities.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    WishlistItem findByWishlistAndProduct(Wishlist wishlist, Product product);

    int deleteByWishlistAndProduct(Wishlist wishlist, Product product);
}
