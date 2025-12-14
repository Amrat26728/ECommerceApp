package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.product.AddProductRequestDto;
import com.amrat.ECommerceApp.entities.Category;
import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.entities.types.ProductImageStatus;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
import com.amrat.ECommerceApp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(AddProductRequestDto addProductRequestDto, Category category, Seller seller){
        Product product = new Product(
                addProductRequestDto.getName(),
                addProductRequestDto.getDescription(),
                addProductRequestDto.getPrice(),
                addProductRequestDto.getStock(),
                ProductImageStatus.UPLOADING,
                category,
                ProductStatus.PENDING_APPROVAL,
                seller
        );

        productRepository.save(product);

        return product;
    }

    public void save(Product product){
        productRepository.save(product);
    }

//    public void setProductStatus(ProductStatus status, Product product){
//        product.saveStatus(status);
//        productRepository.save(product);
//    }
//
//    public void setProductImagesStatus(ProductImageStatus imagesStatus, Product product){
//        product.saveImagesStatus(imagesStatus);
//        productRepository.save(product);
//    }

}
