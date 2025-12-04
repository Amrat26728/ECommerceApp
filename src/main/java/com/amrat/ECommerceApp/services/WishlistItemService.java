package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.repositories.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistItemService {

    private WishlistItemRepository wishlistItemRepository;

}
