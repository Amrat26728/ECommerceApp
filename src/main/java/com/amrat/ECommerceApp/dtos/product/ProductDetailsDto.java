package com.amrat.ECommerceApp.dtos.product;

import com.amrat.ECommerceApp.dtos.category.CategoryIdNameDto;
import com.amrat.ECommerceApp.dtos.productattribute.ProductAttributeDto;
import com.amrat.ECommerceApp.dtos.seller.SellerIdNameDto;
import com.amrat.ECommerceApp.entities.types.ProductImageStatus;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetailsDto {
    private Long id;
    private SellerIdNameDto seller;
    private String name;
    private String description;
    private BigDecimal price;
    private Long stock;
    private CategoryIdNameDto category;
    private List<String> imageUrls;
    private List<ProductAttributeDto> attributes;
    private ProductStatus status;
    private ProductImageStatus imagesStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
