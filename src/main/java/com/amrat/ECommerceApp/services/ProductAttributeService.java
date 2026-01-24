package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.productattribute.ProductAttributeDto;
import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.ProductAttribute;
import com.amrat.ECommerceApp.repositories.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final ModelMapper modelMapper;

    public ProductAttributeDto addAttribute(String name, String value, Product product) {
        ProductAttribute productAttribute = new ProductAttribute(product, name, value);
        productAttribute = productAttributeRepository.save(productAttribute);
        return modelMapper.map(productAttribute, ProductAttributeDto.class);
    }

    public List<ProductAttribute> getAttributes(Product product) {
        return productAttributeRepository.findByProduct(product);
    }

}
