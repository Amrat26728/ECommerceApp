package com.amrat.ECommerceApp.services;

import com.amrat.ECommerceApp.dtos.pageable.BuyerProductPageResponseDto;
import com.amrat.ECommerceApp.dtos.pageable.SellerProductPageResponseDto;
import com.amrat.ECommerceApp.dtos.product.*;
import com.amrat.ECommerceApp.entities.*;
import com.amrat.ECommerceApp.entities.types.ProductImageStatus;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
import com.amrat.ECommerceApp.entities.types.Role;
import com.amrat.ECommerceApp.entities.types.SellerStatus;
import com.amrat.ECommerceApp.projections.BuyerProductCardProjection;
import com.amrat.ECommerceApp.projections.BuyerProductProjection;
import com.amrat.ECommerceApp.projections.PublicProductCardProjection;
import com.amrat.ECommerceApp.projections.PublicProductProjection;
import com.amrat.ECommerceApp.repositories.ProductRepository;
import com.amrat.ECommerceApp.util.CurrentUserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SellerService sellerService;
    private final CurrentUserUtils currentUserUtils;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final ProductAttributeService productAttributeService;

    // add product
    @Transactional
    public AddProductResponseDto addProduct(AddProductRequestDto addProductRequestDto, List<MultipartFile> images, MultipartFile primaryImage) throws IOException {
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
        List<byte[]> primaryImageByte = new ArrayList<>();
        primaryImageByte.add(primaryImage.getBytes());
        cloudinaryService.uploadFilesAsync(imageBytes, primaryImageByte, "products/" + product.getId())
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

    // get Buyer product details
    public SellerProductDetailsDto getSellerProductDetails(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product does not exist."));
        User user = currentUserUtils.getCurrentUser();
        if (user != null && user.getRoles().contains(Role.SELLER) && !product.getSeller().getUser().equals(user)) {
            throw new IllegalArgumentException("This product does not belong to you.");
        }
        return modelMapper.map(product, SellerProductDetailsDto.class);
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
    public SellerProductPageResponseDto getSellersProducts(Integer pageNumber){
        int safePageNumber = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
        User user = currentUserUtils.getCurrentUser();
        Seller seller = sellerService.getSeller(user);
        Page<Product> products = productRepository.findBySeller(seller, PageRequest.of(safePageNumber, 10));
        Page<SellerProductCardDto> productDtoPage = products.map(product -> SellerProductCardDto.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(product.getPrimaryImageUrl())
                .price(product.getPrice())
                .stock(product.getStock())
                .build());

        return SellerProductPageResponseDto.builder()
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

    // public products
    public BuyerProductPageResponseDto publicProducts(Integer pageNumber) {
        int safePageNumber = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
        Page<PublicProductCardProjection> products = productRepository.findByStatus(ProductStatus.ACTIVE.name(), PageRequest.of(safePageNumber, 10));
        List<BuyerProductCardDto> productsList = products.map(product -> BuyerProductCardDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .isInCart(false)
                .isInWishlist(false)
                .sellerId(product.getSellerId())
                .sellerName(product.getSellerName())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategoryId())
                .categoryName(product.getCategoryName())
                .build()).toList();
        return BuyerProductPageResponseDto.builder()
                .content(productsList)
                .isFirst(products.isFirst())
                .isLast(products.isLast())
                .numberOfElements(products.getNumberOfElements())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .build();
    }

    // logged-in user products
    public BuyerProductPageResponseDto buyerProducts(Integer pageNumber) {
        int safePageNumber = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
        User user = currentUserUtils.getCurrentUser();
        Page<BuyerProductCardProjection> products = productRepository.getProductsWithWishlistAndCart(ProductStatus.ACTIVE.name(), user.getId(), PageRequest.of(safePageNumber, 10));
        List<BuyerProductCardDto> productsList = products.map(product -> BuyerProductCardDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getPrimaryImageUrl())
                .sellerId(product.getSellerId())
                .sellerName(product.getSellerName())
                .categoryId(product.getCategoryId())
                .categoryName(product.getCategoryName())
                .isInCart(product.getIsInCart())
                .isInWishlist(product.getIsInWishlist())
                .build()).toList();
        return BuyerProductPageResponseDto.builder()
                .content(productsList)
                .isFirst(products.isFirst())
                .isLast(products.isLast())
                .numberOfElements(products.getNumberOfElements())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .build();
    }

    // public product details
    public BuyerProductDetailsDto getPublicProductDetails(Long productId) throws JsonProcessingException {
        // attributes returned by query is in string
        PublicProductProjection product = productRepository.findPublicProductByIdAndStatus(productId, ProductStatus.ACTIVE.name()).orElseThrow(() -> new IllegalArgumentException("Product does not exist."));
        // convert attributes into Map
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> list = mapper.readValue(
                product.getAttributes(),
                new TypeReference<List<Map<String, String>>>() {}
        );
        Map<String, String> attributes = list.stream()
                .collect(Collectors.toMap(
                        m -> (String) m.get("name"),
                        m -> String.valueOf(m.get("value")),
                        (existing, replacement) -> existing   // keep first
                ));
        return BuyerProductDetailsDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .isInStock(product.getStock() > 0)
                .isInCart(false)
                .isInWishlist(false)
                .primaryImageUrl(product.getPrimaryImageUrl())
                .sellerId(product.getSellerId())
                .sellerName(product.getSellerName())
                .categoryId(product.getCategoryId())
                .categoryName(product.getCategoryName())
                .imageUrls(product.getImageUrls())
                .attributes(attributes)
                .build();
    }

    // logged in product details
    public BuyerProductDetailsDto getProductDetails(Long productId) throws JsonProcessingException {
        User user = currentUserUtils.getCurrentUser();
        // attributes returned by query is in string
        BuyerProductProjection product = productRepository.findProductWishlistAndCart(productId, user.getId(), ProductStatus.ACTIVE.name()).orElseThrow(() -> new IllegalArgumentException("Product does not exist."));
        // convert attributes into Map
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> list = mapper.readValue(
                product.getAttributes(),
                new TypeReference<List<Map<String, String>>>() {}
        );
        Map<String, String> attributes = list.stream()
                .collect(Collectors.toMap(
                        m -> (String) m.get("name"),
                        m -> String.valueOf(m.get("value")),
                        (existing, replacement) -> existing   // keep first
                ));
        return BuyerProductDetailsDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .isInStock(product.getStock() > 0)
                .isInCart(product.getIsInCart())
                .isInWishlist(product.getIsInWishlist())
                .primaryImageUrl(product.getPrimaryImageUrl())
                .sellerId(product.getSellerId())
                .sellerName(product.getSellerName())
                .categoryId(product.getCategoryId())
                .categoryName(product.getCategoryName())
                .imageUrls(product.getImageUrls())
                .attributes(attributes)
                .build();
    }



    //////// read products from .csv file
    public void addProducts(){
        try (Reader reader = new FileReader("products_3001_4000.csv");
             CSVParser csvParser = new CSVParser(
                     reader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim()
             )) {
            for (CSVRecord record : csvParser) {
                List<String> product = new ArrayList<>();
                product.add(record.get("id"));
                product.add(record.get("name"));
                product.add(record.get("description"));
                product.add(record.get("price"));
                product.add(record.get("stock"));
                product.add(record.get("category_id"));
                addProduct2(product);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }

        long start = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        System.out.println("Products added.");
        addProductsAttributes();
        System.out.println("Attributes added.");
        long end = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        long timeTook = end-start;
        System.out.println("Time took: "+timeTook);
    }

    public void addProduct2(List<String> product){
        Category category = categoryService.getCategory(Long.parseLong(product.get(5)));
        Seller seller = sellerService.getSeller(currentUserUtils.getCurrentUser());
        if (!seller.getStatus().equals(SellerStatus.VERIFIED)) {
            throw new IllegalArgumentException("Seller is not verified");
        }
        Product prod = makeProduct2(product, category, seller);

        Product p = productRepository.save(prod);
        p.saveImageUrls(new ArrayList<>());
        productRepository.save(p);
    }

    // just make product and return
    private static Product makeProduct2(List<String> product, Category category, Seller seller) {
        return new Product(
                product.get(1),
                product.get(2),
                new BigDecimal(product.get(3)),
                Long.parseLong(product.get(4)),
                ProductImageStatus.UPLOADED,
                category,
                ProductStatus.ACTIVE,
                seller
        );
    }

    public void addProductsAttributes() {
        int count = 0;
        try (Reader reader = new FileReader("product_attributes_3001_4000.csv");
             CSVParser csvParser = new CSVParser(
                     reader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim()
             )) {
            Product prod = null;
            for (CSVRecord record : csvParser) {
                if (prod == null || prod.getId() != Long.parseLong(record.get("product_id"))) {
                    prod = productRepository.findById(Long.parseLong(record.get("product_id"))).orElse(null);
                }
                productAttributeService.addAttribute(record.get("attribute_title"), record.get("attribute_value"), prod);
                count++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }
        System.out.println(count);
    }

}
