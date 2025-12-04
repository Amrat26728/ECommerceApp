package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.repositories.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private BuyerRepository buyerRepository;

}
