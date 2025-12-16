package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.auth.signup.SellerSignupRequestDto;
import com.amrat.ECommerceApp.dtos.seller.SellerProfileDto;
import com.amrat.ECommerceApp.dtos.seller.SellerProfileUpdateDto;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.repositories.SellerRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import jakarta.persistence.EntityNotFoundException;
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

    // update seller contact and shop description
    @Transactional
    public SellerProfileDto updateSellerProfile(SellerProfileUpdateDto sellerProfileUpdateDto) {
        User user = currentUserUtils.getCurrentUser();
        if (user == null){
            throw new IllegalArgumentException("You are not logged in.");
        }
        Seller seller = sellerRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Seller does not exist."));
        seller.updateSeller(sellerProfileUpdateDto.getContact(), sellerProfileUpdateDto.getShopDescription());
        sellerRepository.save(seller);
        return modelMapper.map(seller, SellerProfileDto.class);
    }

}
