package com.amrat.ECommerceApp.controllers;

import com.amrat.ECommerceApp.dtos.auth.login.LoginRequestDto;
import com.amrat.ECommerceApp.dtos.auth.login.LoginResponseDto;
import com.amrat.ECommerceApp.dtos.auth.ResendVerificationTokenRequestDto;
import com.amrat.ECommerceApp.dtos.auth.signup.BuyerSignupRequestDto;
import com.amrat.ECommerceApp.dtos.auth.signup.SellerSignupRequestDto;
import com.amrat.ECommerceApp.services.AuthService;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestParam String token){
        return ResponseEntity.ok(authService.verify(token));
    }

    @PostMapping("/resend-verification-token")
    public ResponseEntity<Map<String, String>> resendVerificationToken(@RequestBody ResendVerificationTokenRequestDto resendVerificationTokenRequestDto){
        return ResponseEntity.ok(authService.resendVerificationToken(resendVerificationTokenRequestDto.getEmail()));
    }

}
