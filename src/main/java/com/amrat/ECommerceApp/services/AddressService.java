package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.address.AddAddressRequestDto;
import com.amrat.ECommerceApp.dtos.address.AddressDto;
import com.amrat.ECommerceApp.entities.Address;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.types.Role;
import com.amrat.ECommerceApp.repositories.AddressRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import com.cloudinary.api.exceptions.NotFound;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CurrentUserUtils currentUserUtils;
    private final ModelMapper modelMapper;

    // only logged-in user can add address
    @Transactional
    public void addAddress(AddAddressRequestDto addAddressRequestDto) throws AuthenticationException, AccessDeniedException {
        User user = currentUserUtils.getCurrentUser();
        currentUserUtils.validateUser(user);
        if (!user.getRoles().contains(Role.BUYER)) {
            throw new AccessDeniedException("You can not add address.");
        }
        Address address = getAddress(addAddressRequestDto, user);
        addressRepository.save(address);
        unsetDefault(user.getId(), address.getId());
    }

    // make address object
    private static Address getAddress(AddAddressRequestDto addAddressRequestDto, User user) {
        Address address;
        if (addAddressRequestDto.getIsDefault()) {
            address = new Address(user, addAddressRequestDto.getStreetAddress(), addAddressRequestDto.getLandmark(), addAddressRequestDto.getCity(), addAddressRequestDto.getState(), addAddressRequestDto.getPostalCode(), true);
        } else {
            address = new Address(user, addAddressRequestDto.getStreetAddress(), addAddressRequestDto.getLandmark(), addAddressRequestDto.getCity(), addAddressRequestDto.getState(), addAddressRequestDto.getPostalCode(), false);
        }
        return address;
    }

    // change default address
    @Transactional
    public void changeDefault(Long addressId) throws AuthenticationException, NotFound {
        User user = currentUserUtils.getCurrentUser();
        currentUserUtils.validateUser(user);
        Address address = addressRepository.findByIdAndUserId(addressId, user.getId());
        if (address == null) {
            throw new NotFound("404 Not Found");
        }
        unsetDefault(user.getId(), addressId);
        address.setDefaultAddress(true);
    }

    // unset default all other addresses
    private void unsetDefault(Long userId, Long addressId) {
        addressRepository.unsetAllOtherAddresses(userId, addressId);
    }

    // get all addresses of logged-in user
    public List<AddressDto> getAllAddresses() throws AuthenticationException {
        User user = currentUserUtils.getCurrentUser();
        currentUserUtils.validateUser(user);
        List<Address> addresses = addressRepository.findByUser(user);
        return addresses.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    // get details of particular address
    public AddressDto getUserAddress(Long addressId) throws AuthenticationException, NotFound {
        User user = currentUserUtils.getCurrentUser();
        currentUserUtils.validateUser(user);
        Address address = addressRepository.findByIdAndUserId(addressId, user.getId());
        if (address == null) {
            throw new NotFound("404 Not Found");
        }
        return modelMapper.map(address, AddressDto.class);
    }

}
