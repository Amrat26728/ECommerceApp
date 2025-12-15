package com.amrat.ECommerceApp.controllers;

import com.amrat.ECommerceApp.dtos.category.CategoryResponseDto;
import com.amrat.ECommerceApp.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PublicController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategories(){
        return ResponseEntity.ok(categoryService.getCategories());
    }

}
