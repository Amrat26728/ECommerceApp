package com.amrat.ECommerceApp.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String attributeName;

    private String attributeValue;

    public ProductAttribute(Product product, String attributeName, String attributeValue) {
        this.product = product;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public void saveProduct(Product product){
        this.product = product;
    }

}