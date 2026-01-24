package com.amrat.ECommerceApp.util;

import com.amrat.ECommerceApp.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;

@Component
public class CurrentUserUtils {

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        return null; // anonymousUser or other principal types
    }

    public void validateUser(User user) throws AuthenticationException {
        if (user == null) {
            throw new AuthenticationException("Login first.");
        }
    }

}
