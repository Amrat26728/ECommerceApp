package com.amrat.ECommerceApp.services;

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
    public User createUser(String username, String password, Role role){
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null){
            throw new IllegalArgumentException("User already exists.");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        if (password.isEmpty()){
            throw new IllegalArgumentException("Password is required.");
        }

        user = new User(username, passwordEncoder.encode(password), roles);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User does not exist."));
    }

    public void save(User user){
        userRepository.save(user);
    }

}
