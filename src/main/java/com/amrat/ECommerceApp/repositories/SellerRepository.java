package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
