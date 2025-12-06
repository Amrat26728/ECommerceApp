package com.amrat.ECommerceApp.dtos.sellerdtos;

import lombok.Data;

@Data
public class SellerSignupRequestDto {

    private String username;
    private String password;
    private String role;
    private String fullName;
    private String shopName;
    private String shopDescription;
    private String contact;

}
