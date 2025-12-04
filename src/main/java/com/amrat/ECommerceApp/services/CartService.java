package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private CartRepository cartRepository;

}