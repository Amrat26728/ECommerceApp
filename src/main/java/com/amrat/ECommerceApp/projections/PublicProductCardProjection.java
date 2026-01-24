package com.amrat.ECommerceApp.projections;


import java.math.BigDecimal;

public interface PublicProductCardProjection {
    Long getId();
    String getName();
    BigDecimal getPrice();
    Long getSellerId();
    String getSellerName();
    String getImageUrl();
    Long getCategoryId();
    String getCategoryName();
}
