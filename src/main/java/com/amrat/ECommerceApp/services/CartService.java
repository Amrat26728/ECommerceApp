package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.cart.CartAction;
import com.amrat.ECommerceApp.dtos.cart.CartResponseDto;
import com.amrat.ECommerceApp.dtos.wishlist.WishlistAction;
import com.amrat.ECommerceApp.dtos.wishlist.WishlistResponseDto;
import com.amrat.ECommerceApp.entities.*;
import com.amrat.ECommerceApp.repositories.CartRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final CurrentUserUtils currentUserUtils;
    private final ProductService productService;

    @Transactional
    public CartResponseDto toggleCart(Long productId) throws AccessDeniedException {
        User user = currentUserUtils.getCurrentUser();
        if (user == null) {
            throw new AccessDeniedException("Login first.");
        }
        Product product = productService.getProduct(productId);
        Cart cart = cartRepository
                .findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
        int deleted = cartItemService
                .deleteItem(cart, product);
        if (deleted > 0) {
            return CartResponseDto.builder()
                    .success(true)
                    .action(CartAction.REMOVED)
                    .message("Product removed from cart")
                    .isInCart(false)
                    .build();
        }
        cartItemService.addItem(new CartItem(cart, product, product.getPrice()));
        return CartResponseDto.builder()
                .success(true)
                .action(CartAction.ADDED)
                .message("Product added to cart")
                .isInCart(true)
                .build();
    }

}