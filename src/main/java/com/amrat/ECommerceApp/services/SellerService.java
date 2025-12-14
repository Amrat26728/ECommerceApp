package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.auth.signup.SellerSignupRequestDto;
import com.amrat.ECommerceApp.dtos.product.AddProductRequestDto;
import com.amrat.ECommerceApp.dtos.product.AddProductResponseDto;
import com.amrat.ECommerceApp.dtos.seller.SellerProfileDto;
import com.amrat.ECommerceApp.entities.Category;
import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.entities.types.ProductImageStatus;
import com.amrat.ECommerceApp.repositories.SellerRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final CurrentUserUtils currentUserUtils;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public Seller createSeller(User user, SellerSignupRequestDto sellerSignupRequestDto){
        Seller seller = new Seller(
                user,
                sellerSignupRequestDto.getFullName(),
                sellerSignupRequestDto.getShopName(),
                sellerSignupRequestDto.getShopDescription(),
                sellerSignupRequestDto.getContact()
        );

        return sellerRepository.save(seller);
    }

    public SellerProfileDto sellerProfile(){
        // get user
        User user = currentUserUtils.getCurrentUser();
        // get seller data from DB
        Seller seller = getSeller(user);
        // change entity to dto and return
        return modelMapper.map(seller, SellerProfileDto.class);
    }

    public Seller getSeller(User user) {
        return sellerRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("Seller does not exist."));
    }

    // add product
    @Transactional
    public AddProductResponseDto addProduct(AddProductRequestDto addProductRequestDto, List<MultipartFile> images){
        Category category = categoryService.getCategory(addProductRequestDto.getCategoryId());
        Seller seller = getSeller(currentUserUtils.getCurrentUser());
        Product product = productService.addProduct(addProductRequestDto, category, seller);

        /////// upload images
        // uploading images in MultipartFile asynchronously (@Async) after the HTTP request has already finished.
        // Spring/Tomcat stores uploaded files as temporary files
        // When the request ends:
        // Tomcat deletes the temp multipart files
        // Then your async thread runs and tries:
        // file.getBytes()
        // But the temp file is already deleted, so you get:
        // java.nio.file.NoSuchFileException: upload_xxx.tmp

        // Convert MultipartFile to byte[] BEFORE async
        List<byte[]> imageBytes = images.stream()
                .map(file -> {
                    try {
                        return file.getBytes();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        cloudinaryService.uploadFilesAsync(imageBytes, "products/" + product.getId())
                .thenAccept(urls -> {
                    product.saveImageUrls(urls);
                    product.saveImagesStatus(ProductImageStatus.UPLOADED);
                    productService.save(product);
                    System.out.println("Images are added");
                })
                .exceptionally(ex -> {
                    product.saveImagesStatus(ProductImageStatus.UPLOADING_FAILED);
                    productService.save(product);
                    System.out.println("Images uploading failed.");
                    throw new IllegalArgumentException("Image uploading failed.");
                });

        return AddProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .seller(product.getSeller().getId())
                .category(product.getCategory().getId())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .build();
    }

}
