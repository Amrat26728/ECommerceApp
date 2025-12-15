package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findBySellerAndStatusNot(Seller seller, ProductStatus status, Pageable pageable);
}
