package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.pageable.ProductPageResponseDto;
import com.amrat.ECommerceApp.dtos.product.AddProductRequestDto;
import com.amrat.ECommerceApp.dtos.product.AddProductResponseDto;
import com.amrat.ECommerceApp.dtos.product.ProductDetailsDto;
import com.amrat.ECommerceApp.dtos.product.ProductDto;
import com.amrat.ECommerceApp.entities.*;
import com.amrat.ECommerceApp.entities.types.ProductImageStatus;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
import com.amrat.ECommerceApp.entities.types.SellerStatus;
import com.amrat.ECommerceApp.repositories.ProductRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SellerService sellerService;
    private final CurrentUserUtils currentUserUtils;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    // add product
    @Transactional
    public AddProductResponseDto addProduct(AddProductRequestDto addProductRequestDto, List<MultipartFile> images){
        Category category = categoryService.getCategory(addProductRequestDto.getCategoryId());
        Seller seller = sellerService.getSeller(currentUserUtils.getCurrentUser());
        if (!seller.getStatus().equals(SellerStatus.VERIFIED)) {
            throw new IllegalArgumentException("Seller is not verified");
        }
        Product prod = makeProduct(addProductRequestDto, category, seller);

        Product product = productRepository.save(prod);

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
                    productRepository.save(product);
                    System.out.println("Images are added");
                })
                .exceptionally(ex -> {
                    product.saveImagesStatus(ProductImageStatus.UPLOADING_FAILED);
                    productRepository.save(product);
                    System.out.println("Images uploading failed.");
                    throw new IllegalArgumentException("Image uploading failed.");
                });

        return AddProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory().getName())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .build();
    }

    // just make product and return
    private static Product makeProduct(AddProductRequestDto addProductRequestDto, Category category, Seller seller) {
        Product prod = new Product(
                addProductRequestDto.getName(),
                addProductRequestDto.getDescription(),
                addProductRequestDto.getPrice(),
                addProductRequestDto.getStock(),
                ProductImageStatus.UPLOADING,
                category,
                ProductStatus.PENDING_APPROVAL,
                seller
        );

        if (addProductRequestDto.getAttributes() != null && !addProductRequestDto.getAttributes().isEmpty()) {
            Map<String, String> productAttributes = addProductRequestDto.getAttributes();
            productAttributes.forEach((key, value) -> {
                prod.addAttribute(new ProductAttribute(prod, key, value));
            });
        }

        return prod;
    }

    // get product details
    public ProductDetailsDto getProductDetails(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product does not exist."));
        if (!product.getStatus().equals(ProductStatus.ACTIVE)){
            throw new IllegalArgumentException("Product is not available right now.");
        }

        return modelMapper.map(product, ProductDetailsDto.class);
    }

    // get product by id
    public Product getProduct(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product does not exist."));
    }

    // delete product by id
    @Transactional
    public void deleteProduct(Long productId) throws AccessDeniedException {
        Product product = getProduct(productId);
        User user = currentUserUtils.getCurrentUser();
        if (!product.getSeller().getId().equals(user.getId())){
            throw new AccessDeniedException("You are not allowed to delete this product.");
        }
        if (product.getStatus().equals(ProductStatus.DELETED)){
            throw new EntityNotFoundException("Product does not exist.");
        }
        product.saveStatus(ProductStatus.DELETED);
        productRepository.save(product);
    }

    // seller's products
    public ProductPageResponseDto getProducts(Integer pageNumber){
        int safePageNumber = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
        User user = currentUserUtils.getCurrentUser();
        Seller seller = sellerService.getSeller(user);
        Page<Product> products = productRepository.findBySellerAndStatusNot(seller, ProductStatus.DELETED, PageRequest.of(safePageNumber, 10));
        Page<ProductDto> productDtoPage = products.map(product -> ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory().getName())
                .imageUrls(product.getImageUrls())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build());

        return ProductPageResponseDto.builder()
                .content(productDtoPage.getContent())
                .isLast(productDtoPage.isLast())
                .isFirst(productDtoPage.isFirst())
                .pageSize(productDtoPage.getSize())
                .pageNumber(productDtoPage.getNumber())
                .numberOfElements(productDtoPage.getNumberOfElements())
                .totalElements(productDtoPage.getTotalElements())
                .totalPages(productDtoPage.getTotalPages())
                .build();
    }

}
