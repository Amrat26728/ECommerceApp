package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.repositories.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private CartItemRepository cartItemRepository;

}
