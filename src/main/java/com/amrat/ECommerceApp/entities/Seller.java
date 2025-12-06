package com.amrat.ECommerceApp.entities;

import com.amrat.ECommerceApp.entities.types.SellerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @MapsId
    private User user;

    private String fullName;

    private String shopName;

    private String shopDescription;

    private String contact;

    @Enumerated(value = EnumType.STRING)
    private SellerStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Seller(User user, String fullName, String shopName, String shopDescription, String contact){
        this.user = user;
        this.fullName = fullName;
        this.shopName = shopName;
        this.shopDescription = shopDescription;
        this.contact = contact;
        this.status = SellerStatus.PENDING;
    }

}
