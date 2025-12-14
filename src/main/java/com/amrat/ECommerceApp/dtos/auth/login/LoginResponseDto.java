package com.amrat.ECommerceApp.dtos.auth.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private Long id;
    private String token;

}
