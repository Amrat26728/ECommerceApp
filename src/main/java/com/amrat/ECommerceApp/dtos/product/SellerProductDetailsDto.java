package com.amrat.ECommerceApp.dtos.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class SellerProductDetailsDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long stock;
    private Long categoryId;
    private String categoryName;
    private List<String> imageUrls;
    private String primaryImageUrl;
    private Map<String, String> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
