package com.amrat.ECommerceApp.dtos.auth;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String username;
    private String password;

}
