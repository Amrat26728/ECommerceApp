package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
