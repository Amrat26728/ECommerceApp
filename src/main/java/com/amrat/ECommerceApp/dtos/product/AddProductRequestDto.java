package com.amrat.ECommerceApp.dtos.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddProductRequestDto {

    private String name;
    private String description;
    private BigDecimal price;
    private Long stock;
    private Long sellerId;
    private Long categoryId;

}
