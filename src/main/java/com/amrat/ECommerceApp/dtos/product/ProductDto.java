package com.amrat.ECommerceApp.dtos.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long stock;
    private String seller;
    private String category;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
