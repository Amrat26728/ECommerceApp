package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
