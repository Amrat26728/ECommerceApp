package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.repositories.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductAttributeService {

    private ProductAttributeRepository productAttributeRepository;

}
