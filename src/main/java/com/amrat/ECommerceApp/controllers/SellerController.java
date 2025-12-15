package com.amrat.ECommerceApp.controllers;

import com.amrat.ECommerceApp.dtos.pageable.ProductPageResponseDto;
import com.amrat.ECommerceApp.dtos.product.AddProductRequestDto;
import com.amrat.ECommerceApp.dtos.product.AddProductResponseDto;
import com.amrat.ECommerceApp.dtos.seller.SellerProfileDto;
import com.amrat.ECommerceApp.services.CategoryService;
import com.amrat.ECommerceApp.services.ProductService;
import com.amrat.ECommerceApp.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;
    private final ProductService productService;

    @GetMapping("/me")
    public ResponseEntity<SellerProfileDto> getSellerProfile(){
        return ResponseEntity.ok(sellerService.sellerProfile());
    }

    @PostMapping("/products")
    public ResponseEntity<AddProductResponseDto> addProduct(@ModelAttribute AddProductRequestDto addProductRequestDto, @RequestParam("images")List<MultipartFile> images){
        return ResponseEntity.ok(productService.addProduct(addProductRequestDto, images));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws AccessDeniedException {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public ResponseEntity<ProductPageResponseDto> getProducts(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(productService.getProducts(pageNumber));
    }

}
