package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.sellerdtos.SellerSignupRequestDto;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.repositories.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    @Transactional
    public Seller createVendor(User user, SellerSignupRequestDto sellerSignupRequestDto){
        Seller seller = new Seller(
                user,
                sellerSignupRequestDto.getFullName(),
                sellerSignupRequestDto.getShopName(),
                sellerSignupRequestDto.getShopDescription(),
                sellerSignupRequestDto.getContact()
        );

        return sellerRepository.save(seller);
    }

}
