package com.amrat.ECommerceApp.controllers;

import com.amrat.ECommerceApp.dtos.category.CategoryResponseDto;
import com.amrat.ECommerceApp.dtos.product.AddProductRequestDto;
import com.amrat.ECommerceApp.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;

    // add category
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody AddProductRequestDto addProductRequestDto){
        return ResponseEntity.ok(categoryService.addCategory(addProductRequestDto));
    }

}
