package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.VerificationToken;
import com.amrat.ECommerceApp.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public boolean createVerificationToken(String token, User user){
        VerificationToken verificationToken = new VerificationToken(token, user, LocalDateTime.now().plusMinutes(30));
        verificationTokenRepository.save(verificationToken);
        return true;
    }

}
