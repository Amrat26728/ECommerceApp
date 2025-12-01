package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
