package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}
