package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
