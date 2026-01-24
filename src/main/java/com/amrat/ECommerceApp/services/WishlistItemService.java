package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.Wishlist;
import com.amrat.ECommerceApp.entities.WishlistItem;
import com.amrat.ECommerceApp.repositories.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistItemService {

    private final WishlistItemRepository wishlistItemRepository;

    public WishlistItem getWishlistItem(Wishlist wishlist, Product product) {
        return wishlistItemRepository.findByWishlistAndProduct(wishlist, product);
    }

    @Transactional
    public void addItem(WishlistItem wishlistItem) {
        wishlistItemRepository.save(wishlistItem);
    }

    public int deleteItem(Wishlist wishlist, Product product) {
        return wishlistItemRepository.deleteByWishlistAndProduct(wishlist, product);
    }

    @Transactional
    public void deleteItem(Long wishlistItemId) {
        wishlistItemRepository.deleteById(wishlistItemId);
    }
}
