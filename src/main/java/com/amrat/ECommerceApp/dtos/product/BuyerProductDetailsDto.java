package com.amrat.ECommerceApp.dtos.product;

import com.amrat.ECommerceApp.dtos.productattribute.ProductAttributeDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class BuyerProductDetailsDto {

    private Long id;
    private String name;
    private Long sellerId;
    private String sellerName;
    private String description;
    private BigDecimal price;
    private boolean isInStock;
    private boolean isInWishlist;
    private boolean isInCart;
    private Long categoryId;
    private String categoryName;
    private List<String> imageUrls;
    private String primaryImageUrl;
    private Map<String, String> attributes;

}
