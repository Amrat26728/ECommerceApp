package com.amrat.ECommerceApp.dtos.seller;

import lombok.Data;

@Data
public class SellerSignupResponseDto {

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String shopName;
    private String shopDescription;
    private String contact;

}
