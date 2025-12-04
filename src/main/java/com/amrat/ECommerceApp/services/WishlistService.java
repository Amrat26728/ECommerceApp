package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.repositories.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private WishlistRepository wishlistRepository;

}
