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
    private String province;

    @Column(nullable = false)
    private String postalCode;

    private boolean isDefault = false; // default address for checkout

    public Address(User user, String streetAddress,
                   String landmark, String city, String state,
                   String postalCode, boolean isDefault) {
        this.user = user;
        this.streetAddress = streetAddress;
        this.landmark = landmark;
        this.city = city;
        this.province = state;
        this.postalCode = postalCode;
        this.isDefault = isDefault;
    }

}

