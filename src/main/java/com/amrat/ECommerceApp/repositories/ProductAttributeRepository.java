package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    List<ProductAttribute> findByProduct(Product product);
}
