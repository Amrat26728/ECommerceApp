package com.amrat.ECommerceApp.dtos.auth.signup;

import lombok.Data;

@Data
public class BuyerSignupRequestDto {

    private String username;
    private String password;
    private String fullName;
    private String contact;

}
