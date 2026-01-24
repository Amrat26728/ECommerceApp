package com.amrat.ECommerceApp.projections;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PublicProductProjection {
    Long getId();
    String getName();

    Long getSellerId();
    String getSellerName();

    String getDescription();
    BigDecimal getPrice();
    Long getStock();

    Long getCategoryId();
    String getCategoryName();

    List<String> getImageUrls();
    String getPrimaryImageUrl();
    String getAttributes();
}
