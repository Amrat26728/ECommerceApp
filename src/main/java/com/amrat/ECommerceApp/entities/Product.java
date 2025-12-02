package com.amrat.ECommerceApp.entities;

import com.amrat.ECommerceApp.entities.types.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vendor vendorId;

    private String name;

    private String description;

    private Long price;

    private Long stock;

    @ManyToOne
    private Category category;

    private String imageUrl;

    private ProductStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
