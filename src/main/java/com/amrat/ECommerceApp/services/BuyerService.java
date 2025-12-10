package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.buyer.BuyerSignupRequestDto;
import com.amrat.ECommerceApp.entities.Buyer;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.repositories.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final BuyerRepository buyerRepository;

    @Transactional
    public Buyer createBuyer(User user, BuyerSignupRequestDto buyerSignupRequestDto){
        Buyer buyer = new Buyer(user, buyerSignupRequestDto.getFullName(), buyerSignupRequestDto.getContact());
        return buyerRepository.save(buyer);
    }

}
