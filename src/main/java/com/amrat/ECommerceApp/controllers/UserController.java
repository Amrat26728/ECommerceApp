package com.amrat.ECommerceApp.controllers;

import com.amrat.ECommerceApp.dtos.address.AddAddressRequestDto;
import com.amrat.ECommerceApp.dtos.address.AddressDto;
import com.amrat.ECommerceApp.services.AddressService;
import com.cloudinary.api.exceptions.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<Void> addAddress(@RequestBody AddAddressRequestDto addAddressRequestDto) throws AccessDeniedException, AuthenticationException {
        addressService.addAddress(addAddressRequestDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAllAddresses() throws AuthenticationException {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @PutMapping("/addresses/set-default/{addressId}")
    public ResponseEntity<Void> changeDefault(@PathVariable Long addressId) throws AuthenticationException, NotFound{
        addressService.changeDefault(addressId);
        return ResponseEntity.status(202).build();
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> getUserAddress(@PathVariable Long addressId) throws AuthenticationException, NotFound {
        return ResponseEntity.ok(addressService.getUserAddress(addressId));
    }
}
