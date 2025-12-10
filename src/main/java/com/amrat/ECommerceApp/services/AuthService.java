package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.auth.LoginRequestDto;
import com.amrat.ECommerceApp.dtos.auth.LoginResponseDto;
import com.amrat.ECommerceApp.dtos.buyer.BuyerSignupRequestDto;
import com.amrat.ECommerceApp.dtos.seller.SellerSignupRequestDto;
import com.amrat.ECommerceApp.entities.Buyer;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.entities.VerificationToken;
import com.amrat.ECommerceApp.entities.types.Role;
import com.amrat.ECommerceApp.entities.types.SellerStatus;
import com.amrat.ECommerceApp.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final SellerService sellerService;
    private final BuyerService buyerService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;

    // seller signup service
    @Transactional
    public Map<String, String> sellerSignup(SellerSignupRequestDto sellerSignupRequestDto) {
        // create user with role
        User user = userService.createUser(sellerSignupRequestDto.getUsername(), sellerSignupRequestDto.getPassword(), Role.SELLER);

        // save seller info
        Seller seller = sellerService.createVendor(user, sellerSignupRequestDto);

        if (seller == null){
            throw new IllegalArgumentException("Seller could not created.");
        }

        // create verification token
        String token = verificationTokenService.createVerificationToken(user);

        if (token == null){
            throw new IllegalArgumentException("Something wrong happened. try again.");
        }

        System.out.println(token);

        emailService.sendVerificationEmail(sellerSignupRequestDto.getUsername(), token);

        return Map.of("message", "Verification link has been sent to your email.");
    }

    // buyer signup service
    @Transactional
    public Map<String, String> buyerSignup(BuyerSignupRequestDto buyerSignupRequestDto) {
        // create user
        User user = userService.createUser(buyerSignupRequestDto.getUsername(), buyerSignupRequestDto.getPassword(), Role.BUYER);

        // save buyer info
        Buyer buyer = buyerService.createBuyer(user, buyerSignupRequestDto);

        if (buyer == null){
            throw new IllegalArgumentException("Buyer could not created.");
        }

        // create verification token
        String token = verificationTokenService.createVerificationToken(user);

        if (token == null){
            throw new IllegalArgumentException("Something wrong happened. try again.");
        }

        emailService.sendVerificationEmail(buyerSignupRequestDto.getUsername(), token);

        return Map.of("message", "Verification link has been sent to your email.");
    }


    // login service
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        // for user null safety
        User user = Optional.ofNullable(authentication.getPrincipal())
                .filter(p -> p instanceof User)
                .map(p -> (User) p)
                .orElseThrow(() -> new IllegalStateException("User principal is missing"));

        if (!user.isVerified()){
            throw new IllegalArgumentException("Account is not verified.");
        }

        if (!user.isActive()){
            throw new IllegalArgumentException("Account is inactivated.");
        }

        if (user.getRoles().contains(Role.SELLER)){
            Seller seller = sellerService.getSeller(user);
            if (seller.getStatus() == SellerStatus.PENDING){
                throw new IllegalArgumentException("Your account is not verified yet.");
            }
        }

        String token = authUtil.generateAccessToken(user);

        System.out.println("Roles: "+user.getRoles());

        return new LoginResponseDto(user.getId(), token);
    }

    // verify user
    @Transactional
    public Map<String, String> verify(String token) {

        VerificationToken verificationToken = verificationTokenService.getByToken(token);

        if (verificationToken == null) {
            throw new IllegalArgumentException("You have not created account yet. create your account first.");
        }
        if (!passwordEncoder.matches(token, verificationToken.getHashedToken())){
            throw new IllegalArgumentException("Invalid token.");
        }
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            // send email on user email and then throw exception
            throw new IllegalArgumentException("Verification token expired. Token is sent to your email.");
        }
        User user = verificationToken.getUser();

        if (user.isVerified()) {
            throw new IllegalStateException("User already verified");
        }

        user.verifyAccount();
        userService.save(user);

        verificationTokenService.delete(verificationToken);

        return Map.of("message", "Account verified successfully. You can now log in.");
    }

    // resend user verification token
    @Transactional
    public Map<String, String> resendVerificationToken(String email) {

        User user = userService.getUser(email);

        if (user.isVerified()) {
            throw new IllegalStateException("User already verified");
        }

        // Delete any old tokens for this user
        VerificationToken verificationToken = verificationTokenService.getByUser(user);

        if (verificationToken != null){
            verificationTokenService.delete(verificationToken);
        }

        // Create new token
        String token = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder.encode(token);

        VerificationToken newToken = new VerificationToken(token, hashedToken, user, LocalDateTime.now().plusMinutes(30));

        verificationTokenService.save(newToken);

//        emailService.sendVerificationEmail(email, token);

        return Map.of("message", "A verification link has been sent to your email.");
    }

}
