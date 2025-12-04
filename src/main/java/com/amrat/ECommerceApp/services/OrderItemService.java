package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private OrderItemRepository orderItemRepository;

}
