package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
