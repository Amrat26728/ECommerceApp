package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorService {

    private VendorRepository vendorRepository;

}
