package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.category.AddCategoryResponseDto;
import com.amrat.ECommerceApp.dtos.product.AddProductRequestDto;
import com.amrat.ECommerceApp.entities.Category;
import com.amrat.ECommerceApp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public Category getCategory(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category does not exist."));
    }

    @Transactional
    public AddCategoryResponseDto addCategory(AddProductRequestDto addProductRequestDto){
        Category category = new Category(addProductRequestDto.getName(), addProductRequestDto.getDescription());
        categoryRepository.save(category);
        return modelMapper.map(category, AddCategoryResponseDto.class);
    }

}
