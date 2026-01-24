package com.amrat.ECommerceApp.controllers;

import com.amrat.ECommerceApp.dtos.cart.CartResponseDto;
import com.amrat.ECommerceApp.dtos.category.CategoryResponseDto;
import com.amrat.ECommerceApp.dtos.pageable.BuyerProductPageResponseDto;
import com.amrat.ECommerceApp.dtos.product.BuyerProductDetailsDto;
import com.amrat.ECommerceApp.dtos.wishlist.WishlistResponseDto;
import com.amrat.ECommerceApp.services.*;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final WishlistService wishlistService;
    private final WishlistItemService wishlistItemService;
    private final CurrentUserUtils currentUserUtils;
    private final CartService cartService;
    private final CartItemService cartItemService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategories(){
        return ResponseEntity.ok(categoryService.getCategories());
    }

    // tested
    // get products
    @GetMapping("/products")
    public ResponseEntity<BuyerProductPageResponseDto> getProducts(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber) {
        if (currentUserUtils.getCurrentUser() == null) {
            System.out.println("Public products.");
            return ResponseEntity.ok(productService.publicProducts(pageNumber));
        }
        System.out.println("Logged in products.");
        return ResponseEntity.ok(productService.buyerProducts(pageNumber));
    }

    // tested
    @GetMapping("/products/{productId}")
    public ResponseEntity<BuyerProductDetailsDto> getProduct(@PathVariable Long productId) throws JsonProcessingException {
        if (currentUserUtils.getCurrentUser() == null) {
            System.out.println("public product.");
            return ResponseEntity.ok(productService.getPublicProductDetails(productId));
        }
        System.out.println("Logged in product.");
        return ResponseEntity.ok(productService.getProductDetails(productId));
    }

    // tested
    @PostMapping("/wishlist/toggle/{productId}")
    public ResponseEntity<WishlistResponseDto> toggleWishlistItem(@PathVariable Long productId) throws AccessDeniedException {
        return ResponseEntity.ok(wishlistService.toggleWishlist(productId));
    }

    // tested
    @DeleteMapping("/wishlist/{wishlistItem}")
    public ResponseEntity<Void> deleteWishlistItem(@PathVariable Long wishlistItemId) {
        wishlistItemService.deleteItem(wishlistItemId);
        return ResponseEntity.noContent().build();
    }

    // tested
    @PostMapping("/cart/toggle/{productId}")
    public ResponseEntity<CartResponseDto> toggleCartItem(@PathVariable Long productId) throws AccessDeniedException {
        return ResponseEntity.ok(cartService.toggleCart(productId));
    }

    // tested
    @PutMapping("/cart/{cartItemId}/{quantity}")
    public ResponseEntity<Void> changeQuantity(@PathVariable Long cartItemId, @PathVariable Long quantity) throws AccessDeniedException {
        cartItemService.changeQuantity(cartItemId, quantity);
        return ResponseEntity.noContent().build();
    }

    // tested
    @DeleteMapping("/cart/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) throws AccessDeniedException{
        cartItemService.deleteItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/wishlist/{productId}")
//    public ResponseEntity<WishlistResponseDto> addWishlistItem(@PathVariable Long productId) {
//        return ResponseEntity.ok(wishlistService.addProductToWishlist(productId));
//    }
//
//    @DeleteMapping("/wishlist/{productId}")
//    public ResponseEntity<WishlistResponseDto> removeProductFromWishlist(@PathVariable Long productId) {
//        return ResponseEntity.ok(wishlistService.removeProductFromWishlist(productId));
//    }

}
