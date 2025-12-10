package com.amrat.ECommerceApp.entities;

import com.amrat.ECommerceApp.entities.types.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "app_users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private boolean isVerified;

    private boolean isActive;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(
                role ->authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()))
        );

        return authorities;
    }

    public User(String username, String password, Set<Role> roles){

        if (username == null || username.isEmpty()){
            throw new IllegalArgumentException("Username is required");
        }

        if (password == null || password.isEmpty()){
            throw new IllegalArgumentException("Password are required");
        }

        if (roles == null || roles.isEmpty()){
            throw new IllegalArgumentException("Roles are required");
        }

        this.username = username;
        this.password = password;
        this.roles = new HashSet<>(roles);
        this.isVerified = false;
        this.isActive = false;
    }

    public void verifyAccount(){
        this.isVerified = true;
    }

}
