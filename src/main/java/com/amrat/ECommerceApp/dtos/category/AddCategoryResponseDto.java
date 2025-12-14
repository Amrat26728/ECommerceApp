package com.amrat.ECommerceApp.dtos.category;

import lombok.Data;

@Data
public class AddCategoryResponseDto {
    private Long id;
    private String name;
    private String description;
}
