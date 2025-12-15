package com.amrat.ECommerceApp.dtos.pageable;

import com.amrat.ECommerceApp.dtos.product.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductPageResponseDto {
    private List<ProductDto> content;
    private int pageNumber;
    private int pageSize;
    private int numberOfElements;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
}
