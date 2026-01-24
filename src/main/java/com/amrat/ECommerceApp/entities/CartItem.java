package com.amrat.ECommerceApp.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private BigDecimal price;

    private Long quantity;

    private BigDecimal totalPrice;

    public CartItem(Cart cart, Product product, BigDecimal price) {
        this.cart = cart;
        this.product = product;
        this.price = price;
        this.quantity = 1L;
        this.totalPrice = price;
    }

    public void changeQuantity(Long quantity) {
        this.quantity = quantity;
        this.totalPrice = this.price.multiply(BigDecimal.valueOf(quantity));
    }

}
