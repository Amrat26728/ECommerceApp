package com.amrat.ECommerceApp.dtos.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BuyerProductCardDto {
    private Long id;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private boolean isInCart;
    private boolean isInWishlist;
    private Long sellerId;
    private String sellerName;
    private Long categoryId;
    private String categoryName;
}
