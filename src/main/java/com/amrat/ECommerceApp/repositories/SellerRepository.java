package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByUser(User user);

}
