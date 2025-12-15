package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.pageable.ProductPageResponseDto;
import com.amrat.ECommerceApp.dtos.product.AddProductRequestDto;
import com.amrat.ECommerceApp.dtos.product.AddProductResponseDto;
import com.amrat.ECommerceApp.dtos.product.ProductDto;
import com.amrat.ECommerceApp.entities.Category;
import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.entities.User;
import com.amrat.ECommerceApp.entities.types.ProductImageStatus;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
import com.amrat.ECommerceApp.entities.types.SellerStatus;
import com.amrat.ECommerceApp.repositories.ProductRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SellerService sellerService;
    private final CurrentUserUtils currentUserUtils;
    private final CloudinaryService cloudinaryService;

    public void save(Product product){
        productRepository.save(product);
    }

    // add product
    @Transactional
    public AddProductResponseDto addProduct(AddProductRequestDto addProductRequestDto, List<MultipartFile> images){
        Category category = categoryService.getCategory(addProductRequestDto.getCategoryId());
        Seller seller = sellerService.getSeller(currentUserUtils.getCurrentUser());
        if (!seller.getStatus().equals(SellerStatus.VERIFIED)) {
            throw new IllegalArgumentException("Seller is not verified");
        }
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

    public Product getProduct(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product does not exist."));
    }

    // delete product
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
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory().getName())
                .status(product.getStatus())
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
