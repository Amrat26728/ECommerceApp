package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.auth.signup.SellerSignupRequestDto;
import com.amrat.ECommerceApp.dtos.seller.SellerProfileDto;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.repositories.SellerRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final CurrentUserUtils currentUserUtils;

    @Transactional
    public Seller createSeller(User user, SellerSignupRequestDto sellerSignupRequestDto){
        Seller seller = new Seller(
                user,
                sellerSignupRequestDto.getFullName(),
                sellerSignupRequestDto.getShopName(),
                sellerSignupRequestDto.getShopDescription(),
                sellerSignupRequestDto.getContact()
        );

        return sellerRepository.save(seller);
    }

    public SellerProfileDto sellerProfile(){
        // get user
        User user = currentUserUtils.getCurrentUser();
        // get seller data from DB
        Seller seller = getSeller(user);
        // change entity to dto and return
        return modelMapper.map(seller, SellerProfileDto.class);
    }

    public Seller getSeller(User user) {
        return sellerRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("Seller does not exist."));
    }

}
