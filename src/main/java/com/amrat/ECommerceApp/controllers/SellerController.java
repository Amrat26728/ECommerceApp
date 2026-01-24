package com.amrat.ECommerceApp.controllers;

import com.amrat.ECommerceApp.dtos.pageable.SellerProductPageResponseDto;
import com.amrat.ECommerceApp.dtos.product.AddProductRequestDto;
import com.amrat.ECommerceApp.dtos.product.AddProductResponseDto;
import com.amrat.ECommerceApp.dtos.product.SellerProductDetailsDto;
import com.amrat.ECommerceApp.dtos.seller.SellerProfileDto;
import com.amrat.ECommerceApp.dtos.seller.SellerProfileUpdateDto;
import com.amrat.ECommerceApp.services.ProductService;
import com.amrat.ECommerceApp.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;
    private final ProductService productService;

    // get profile
    @GetMapping("/me")
    public ResponseEntity<SellerProfileDto> getSellerProfile(){
        return ResponseEntity.ok(sellerService.sellerProfile());
    }

    // add product
    @PostMapping("/products")
    public ResponseEntity<AddProductResponseDto> addProduct(@ModelAttribute AddProductRequestDto addProductRequestDto, @RequestParam("images") List<MultipartFile> images, @RequestParam("primaryImage") MultipartFile primaryImage) throws IOException {
        return ResponseEntity.ok(productService.addProduct(addProductRequestDto, images, primaryImage));
    }

    // delete product
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws AccessDeniedException {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // get products
    @GetMapping("/products")
    public ResponseEntity<SellerProductPageResponseDto> getProducts(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber){
        return ResponseEntity.ok(productService.getSellersProducts(pageNumber));
    }

    // update profile
    @PutMapping("/me")
    public ResponseEntity<SellerProfileDto> updateProfile(@RequestBody SellerProfileUpdateDto sellerProfileUpdateDto){
        return ResponseEntity.ok(sellerService.updateSellerProfile(sellerProfileUpdateDto));
    }

    // get product details
    @GetMapping("/products/{productId}")
    public ResponseEntity<SellerProductDetailsDto> getProductDetails(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getSellerProductDetails(productId));
    }



    /////// read products from .csv file
    @PostMapping("/add-attributes")
    public ResponseEntity<Map<String, String>> addAttributes() {
        productService.addProductsAttributes();
        return ResponseEntity.ok(Map.of("success", "Attributes are added."));
    }

}
