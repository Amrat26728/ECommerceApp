package com.amrat.ECommerceApp.dtos.buyer;

import lombok.Data;

@Data
public class BuyerSignupRequestDto {

    private String username;
    private String password;
    private String role;
    private String fullName;
    private String contact;

}
