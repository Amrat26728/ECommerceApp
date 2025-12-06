package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.sellerdtos.SellerSignupRequestDto;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final SellerService sellerService;
    private final VerificationTokenService verificationTokenService;

    @Transactional
    public Map<String, String> sellerSignup(SellerSignupRequestDto sellerSignupRequestDto) {
        System.out.println("Vendor service 1");
        User user = userService.createUser(sellerSignupRequestDto);
        System.out.println("Vendor service 2");

        Seller seller = sellerService.createVendor(user, sellerSignupRequestDto);

        // create verification token and send in email
        String token = UUID.randomUUID().toString();

        // create verification token
        boolean verificationTokenCreated = verificationTokenService.createVerificationToken(token, user);

        if (!verificationTokenCreated){
            throw new IllegalArgumentException("Something wrong happened. try again.");
        }

//        emailService.sendVerificationEmail(signupRequestDto.getEmail(), token);

        return Map.of("message", "Verification link has been sent to your email.");
    }

}
