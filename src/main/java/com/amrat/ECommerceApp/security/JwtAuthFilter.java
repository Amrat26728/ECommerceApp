package com.amrat.ECommerceApp.security;

import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.services.UserService;
import com.amrat.ECommerceApp.util.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final AuthUtil authUtil;

    private final HandlerExceptionResolver handlerExceptionResolver;

    // register this filter in the WebSecurityConfig file
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            final String requestTokenHeader = request.getHeader("Authorization");

            if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")){
                filterChain.doFilter(request, response);
                return;
            }

            String token = requestTokenHeader.split("Bearer ")[1];

            String username = authUtil.getUsernameFromToken(token);

            // username should not be null and security context should be null
            // every filter has to fill security context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User user = userService.getUserByUsername(username);

                if (!user.isVerified()){
                    throw new IllegalArgumentException("Account is not verified.");
                }

                if (!user.isActive()){
                    throw new IllegalArgumentException("Account is inactivated. Please contact Admin");
                }

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex){
            // this sends exception to global exception handler
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
