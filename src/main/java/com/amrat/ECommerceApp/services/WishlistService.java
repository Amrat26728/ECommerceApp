package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.wishlist.WishlistAction;
import com.amrat.ECommerceApp.dtos.wishlist.WishlistResponseDto;
import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Wishlist;
import com.amrat.ECommerceApp.entities.WishlistItem;
import com.amrat.ECommerceApp.repositories.WishlistRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemService wishlistItemService;
    private final ProductService productService;
    private final CurrentUserUtils currentUserUtils;

    // add product to wishlist
//    @Transactional
//    public WishlistResponseDto addProductToWishlist(Long productId) throws AccessDeniedException {
//        User user = currentUserUtils.getCurrentUser();
//        if (user == null) {
//            throw new AccessDeniedException("Login first.");
//        }
//        Product product = productService.getProduct(productId);
//        Wishlist wishlist = wishlistRepository
//                .findByUser(user)
//                .orElseGet(() -> wishlistRepository.save(new Wishlist(user)));
//        try {
//            wishlistItemService.addItem(
//                    new WishlistItem(wishlist, product)
//            );
//            return WishlistResponseDto.builder()
//                    .success(true)
//                    .action(WishlistAction.ADDED)
//                    .message("Product added to wishlist")
//                    .isInWishlist(true)
//                    .build();
//
//        } catch (Exception ex) {
//            return WishlistResponseDto.builder()
//                    .success(false)
//                    .action(WishlistAction.ALREADY_EXISTS)
//                    .message("Product already in wishlist")
//                    .isInWishlist(true)
//                    .build();
//        }
//    }
//
//    // remove product from wishlist
//    public WishlistResponseDto removeProductFromWishlist(Long productId){
//        User user = currentUserUtils.getCurrentUser();
//        Product product = productService.getProduct(productId);
//        Wishlist wishlist = wishlistRepository.findByUser(user).orElseGet(() -> wishlistRepository.save(new Wishlist(user)));
//        wishlistItemService.deleteItem(wishlist, product);
//        return WishlistResponseDto.builder()
//                .success(true)
//                .action(WishlistAction.REMOVED)
//                .message("Product removed from wishlist")
//                .isInWishlist(false)
//                .build();
//    }

    // toggle wishlist
    @Transactional
    public WishlistResponseDto toggleWishlist(Long productId) throws AccessDeniedException {
        User user = currentUserUtils.getCurrentUser();
        if (user == null) {
            throw new AccessDeniedException("Login first.");
        }
        Product product = productService.getProduct(productId);
        Wishlist wishlist = wishlistRepository
                .findByUser(user)
                .orElseGet(() -> wishlistRepository.save(new Wishlist(user)));
        int deleted = wishlistItemService
                .deleteItem(wishlist, product);
        if (deleted > 0) {
            return WishlistResponseDto.builder()
                    .success(true)
                    .action(WishlistAction.REMOVED)
                    .message("Product removed from wishlist")
                    .isInWishlist(false)
                    .build();
        }
        wishlistItemService.addItem(new WishlistItem(wishlist, product));
        return WishlistResponseDto.builder()
                .success(true)
                .action(WishlistAction.ADDED)
                .message("Product added to wishlist")
                .isInWishlist(true)
                .build();
    }

    public Wishlist findByUser(User user) {
        return wishlistRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("Wishlist does not exist."));
    }
}
