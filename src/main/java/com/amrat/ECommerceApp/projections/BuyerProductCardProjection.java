package com.amrat.ECommerceApp.projections;

import java.math.BigDecimal;

public interface BuyerProductCardProjection {
    Long getId();
    String getName();
    BigDecimal getPrice();
    Long getSellerId();
    String getSellerName();
    Long getStock();
    String getPrimaryImageUrl();
    Long getCategoryId();
    String getCategoryName();
    Boolean getIsInCart();
    Boolean getIsInWishlist();
}
