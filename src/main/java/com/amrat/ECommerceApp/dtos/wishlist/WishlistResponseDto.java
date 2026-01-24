package com.amrat.ECommerceApp.dtos.wishlist;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishlistResponseDto {
    private boolean success;
    private String message;
    private WishlistAction action;
    private boolean isInWishlist;
}