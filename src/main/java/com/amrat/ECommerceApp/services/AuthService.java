package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.buyerdtos.BuyerSignupRequestDto;
import com.amrat.ECommerceApp.dtos.sellerdtos.SellerSignupRequestDto;
import com.amrat.ECommerceApp.entities.Buyer;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.entities.types.Role;
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
    private final BuyerService buyerService;
    private final VerificationTokenService verificationTokenService;

    @Transactional
    public Map<String, String> sellerSignup(SellerSignupRequestDto sellerSignupRequestDto) {
        // create user with role
        User user = userService.createUser(sellerSignupRequestDto.getUsername(), sellerSignupRequestDto.getPassword(), Role.SELLER);

        // save seller info
        Seller seller = sellerService.createVendor(user, sellerSignupRequestDto);

        if (seller == null){
            throw new IllegalArgumentException("Seller could not created.");
        }

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

    @Transactional
    public Map<String, String> buyerSignup(BuyerSignupRequestDto buyerSignupRequestDto) {
        // create user
        User user = userService.createUser(buyerSignupRequestDto.getUsername(), buyerSignupRequestDto.getPassword(), Role.BUYER);

        // save buyer info
        Buyer buyer = buyerService.createBuyer(user, buyerSignupRequestDto);

        if (buyer == null){
            throw new IllegalArgumentException("Buyer could not created.");
        }

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
