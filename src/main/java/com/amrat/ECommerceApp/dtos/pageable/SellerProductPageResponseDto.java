package com.amrat.ECommerceApp.dtos.pageable;

import com.amrat.ECommerceApp.dtos.product.SellerProductCardDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SellerProductPageResponseDto {
    private List<SellerProductCardDto> content;
    private int pageNumber;
    private int pageSize;
    private int numberOfElements;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
}
