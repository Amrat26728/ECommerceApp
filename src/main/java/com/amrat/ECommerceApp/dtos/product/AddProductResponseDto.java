package com.amrat.ECommerceApp.dtos.product;

import com.amrat.ECommerceApp.entities.types.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductResponseDto {

    private Long id;
    private String name;
    private String description;

    private BigDecimal price;
    private Long stock;

    private Long seller;
    private Long category;

    private ProductStatus status;

    private LocalDateTime createdAt;

}
