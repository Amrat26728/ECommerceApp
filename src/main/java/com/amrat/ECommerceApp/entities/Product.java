package com.amrat.ECommerceApp.entities;

import com.amrat.ECommerceApp.entities.types.ProductImageStatus;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Seller seller;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long stock;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private String primaryImageUrl;

//    @Type(ListArrayType.class)
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private List<String> imageUrls;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Enumerated(EnumType.STRING)
    private ProductImageStatus imagesStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Product(String name, String description, BigDecimal price, Long stock, ProductImageStatus imagesStatus, Category category, ProductStatus status, Seller seller){
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.status = status;
        this.seller = seller;
        this.imagesStatus = imagesStatus;
    }

    public void saveImageUrls(List<String> imageUrls){
        this.imageUrls = imageUrls;
    }

    public void saveStatus(ProductStatus status){
        this.status = status;
    }

    public void saveImagesStatus(ProductImageStatus imagesStatus){
        this.imagesStatus = imagesStatus;
    }

    public void addAttribute(ProductAttribute productAttribute) {
        this.attributes.add(productAttribute);
        productAttribute.saveProduct(this); // maintain bidirectional consistency
    }

}
