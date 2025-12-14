package com.amrat.ECommerceApp.dtos.seller;

import com.amrat.ECommerceApp.entities.types.SellerStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SellerProfileDto {

    private Long id;
    private String fullName;
    private String shopName;
    private String shopDescription;
    private String contact;
    private SellerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
