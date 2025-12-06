package com.amrat.ECommerceApp.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @OneToOne
    private Product product;

    private Double price;

    private Integer quantity;

    private Double totalAmount;

}
