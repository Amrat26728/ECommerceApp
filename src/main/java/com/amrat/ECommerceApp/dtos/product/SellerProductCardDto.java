package com.amrat.ECommerceApp.dtos.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SellerProductCardDto {
    private Long id;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private Long stock;
}
