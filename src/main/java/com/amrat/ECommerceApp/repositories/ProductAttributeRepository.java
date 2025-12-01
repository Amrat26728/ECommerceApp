package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
}
