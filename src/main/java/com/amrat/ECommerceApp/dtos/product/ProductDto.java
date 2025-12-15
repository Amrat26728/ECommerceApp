package com.amrat.ECommerceApp.dtos.product;

import com.amrat.ECommerceApp.entities.types.ProductImageStatus;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
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
    private String description;
    private BigDecimal price;
    private Long stock;
    private String seller;
    private String category;
    private List<String> imageUrls;
    private ProductStatus status;
    private ProductImageStatus imagesStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
