package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.VerificationToken;
import com.amrat.ECommerceApp.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public String createVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder.encode(token);
        VerificationToken verificationToken = new VerificationToken(token, hashedToken, user, LocalDateTime.now().plusMinutes(30));
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public VerificationToken getByToken(String token){
        return verificationTokenRepository
                .findByToken(token);
    }

    public VerificationToken getByUser(User user) {
        return verificationTokenRepository.findByUser(user);
    }

    public void delete(VerificationToken verificationToken){
        verificationTokenRepository.delete(verificationToken);
    }

    public void save(VerificationToken verificationToken){
        verificationTokenRepository.save(verificationToken);
    }
}
