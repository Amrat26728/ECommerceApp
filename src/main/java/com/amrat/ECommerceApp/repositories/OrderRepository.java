package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<DeliveryOrder, Long> {
}
