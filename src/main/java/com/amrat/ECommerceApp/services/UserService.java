package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.sellerdtos.SellerSignupRequestDto;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.types.Role;
import com.amrat.ECommerceApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(SellerSignupRequestDto sellerSignupRequestDto){
        User user = userRepository.findByUsername(sellerSignupRequestDto.getUsername()).orElse(null);

        System.out.println(user);

        if (user != null){
            throw new IllegalArgumentException("User already exists.");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(Role.SELLER);

        if (sellerSignupRequestDto.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password is required.");
        }

        user = new User(sellerSignupRequestDto.getUsername(), passwordEncoder.encode(sellerSignupRequestDto.getPassword()), roles);
        return userRepository.save(user);
    }

}
