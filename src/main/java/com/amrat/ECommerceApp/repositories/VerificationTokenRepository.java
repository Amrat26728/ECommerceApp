package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
