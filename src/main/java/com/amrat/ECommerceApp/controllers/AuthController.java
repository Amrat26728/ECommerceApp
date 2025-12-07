package com.amrat.ECommerceApp.controllers;

import com.amrat.ECommerceApp.dtos.buyerdtos.BuyerSignupRequestDto;
import com.amrat.ECommerceApp.dtos.sellerdtos.SellerSignupRequestDto;
import com.amrat.ECommerceApp.services.AuthService;
import com.amrat.ECommerceApp.services.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/seller-signup")
    public ResponseEntity<Map<String, String>> sellerSignup(@RequestBody SellerSignupRequestDto sellerSignupRequestDto){
        return ResponseEntity.ok(authService.sellerSignup(sellerSignupRequestDto));
    }

    @PostMapping("/buyer-signup")
    public ResponseEntity<Map<String, String>> buyerSignup(@RequestBody BuyerSignupRequestDto buyerSignupRequestDto){
        return ResponseEntity.ok(authService.buyerSignup(buyerSignupRequestDto));
    }

}
