package com.amrat.ECommerceApp.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String streetAddress;

    private String landmark; // optional

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String postalCode;

    private boolean isDefault = false; // default address for checkout

    public Address(User user, String streetAddress, String landmark, String city, String state, String postalCode, boolean isDefault) {
        validate(user, streetAddress, city, state, postalCode);
        if (landmark == null) {
            landmark = "";
        }
        this.user = user;
        this.streetAddress = streetAddress;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.isDefault = isDefault;
    }

    private void validate(User user, String streetAddress, String city, String state, String postalCode) {
        if (user == null) {
            throw new IllegalArgumentException("User can not be null.");
        }
        if (streetAddress == null) {
            throw new IllegalArgumentException("Street can not be null.");
        }
        if (city == null) {
            throw new IllegalArgumentException("City can not be null.");
        }
        if (state == null) {
            throw new IllegalArgumentException("State can not be null.");
        }
        if (postalCode == null) {
            throw new IllegalArgumentException("Postal code can not be null.");
        }
    }

    public void setDefaultAddress(boolean def) {
        this.isDefault = def;
    }

}

