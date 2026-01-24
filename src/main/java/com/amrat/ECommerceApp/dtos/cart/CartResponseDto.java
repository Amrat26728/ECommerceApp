package com.amrat.ECommerceApp.dtos.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponseDto {
    private boolean success;
    private String message;
    private CartAction action;
    private boolean isInCart;
}