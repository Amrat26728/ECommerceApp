package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.entities.*;
import com.amrat.ECommerceApp.repositories.CartItemRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CurrentUserUtils currentUserUtils;

    public CartItem getCartItem(Cart cart, Product product) {
        return cartItemRepository.findByCartAndProduct(cart, product);
    }

    @Transactional
    public void addItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    public int deleteItem(Cart cart, Product product) {
        return cartItemRepository.deleteByCartAndProduct(cart, product);
    }

    public void deleteItem(Long cartItemId) throws AccessDeniedException{
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new EntityNotFoundException("Cart item does not exist."));
        if (!Objects.equals(currentUserUtils.getCurrentUser().getId(), cartItem.getCart().getUser().getId())){
            throw new AccessDeniedException("Access denied.");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void changeQuantity(Long cartItemId, Long quantity) throws AccessDeniedException {
        User user = currentUserUtils.getCurrentUser();
        if (user == null) {
            throw new AccessDeniedException("Login first.");
        }
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new EntityNotFoundException("Cart item does not exist."));
        if (!Objects.equals(user.getId(), cartItem.getCart().getUser().getId())) {
            throw new AccessDeniedException("This item does not belong to you.");
        }
        if (quantity <= 0) {
            cartItemRepository.deleteById(cartItemId);
            return;
        }
        cartItem.changeQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

}
