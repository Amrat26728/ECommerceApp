package com.amrat.ECommerceApp.projections;

import java.math.BigDecimal;
import java.util.List;

public interface BuyerProductProjection {
    Long getId();
    String getName();

    Long getSellerId();
    String getSellerName();

    String getDescription();
    BigDecimal getPrice();
    Long getStock();

    Boolean getIsInWishlist();
    Boolean getIsInCart();

    Long getCategoryId();
    String getCategoryName();

    List<String> getImageUrls();
    String getPrimaryImageUrl();
    String getAttributes();
}
