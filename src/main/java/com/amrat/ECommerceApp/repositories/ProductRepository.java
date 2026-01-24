package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Product;
import com.amrat.ECommerceApp.entities.Seller;
import com.amrat.ECommerceApp.entities.types.ProductStatus;
import com.amrat.ECommerceApp.projections.BuyerProductCardProjection;
import com.amrat.ECommerceApp.projections.PublicProductCardProjection;
import com.amrat.ECommerceApp.projections.BuyerProductProjection;
import com.amrat.ECommerceApp.projections.PublicProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findBySeller(Seller seller, Pageable pageable);

    @Query(value = """
            SELECT
            p.id AS id, p.name AS name, s.user_id AS sellerId, s.full_name AS sellerName,
            p.description AS description, p.price AS price, p.stock AS stock, c.id AS categoryId,
            c.name AS categoryName, p.primary_image_url AS primaryImageUrl, p.image_urls AS imageUrls,
            COALESCE(
                    json_agg(
                        json_build_object(
                            'id', pa.id,
                            'name', pa.attribute_name,
                            'value', pa.attribute_value
                        )
                    ) FILTER (WHERE pa.id IS NOT NULL),
                    '[]'
                ) AS attributes
            FROM product p
            JOIN seller s ON s.user_id = p.seller_user_id
            JOIN category c ON c.id = p.category_id
            LEFT JOIN product_attribute pa ON pa.product_id = p.id
            WHERE p.id = :productId
              AND p.status = :status
            GROUP BY
                p.id, s.user_id, c.id""", nativeQuery = true)
    Optional<PublicProductProjection> findPublicProductByIdAndStatus(@Param("productId") Long productId, @Param("status") String status);

    @Query(value = """
            SELECT
            p.id AS id, p.name AS name, s.user_id AS sellerId, s.full_name AS sellerName, p.price AS price, p.stock AS stock, c.id AS categoryId, c.name AS categoryName, p.primary_image_url AS primaryImageUrl
            FROM product p
            JOIN seller s ON s.user_id = p.seller_user_id
            JOIN category c ON
            c.id = p.category_id
            WHERE p.status = :status""", nativeQuery = true)
    Page<PublicProductCardProjection> findByStatus(@Param("status") String status, Pageable pageable);

    @Query(value = """
            SELECT
            p.id AS id, p.name AS name, s.user_id AS sellerId, s.full_name AS sellerName, p.price AS price, p.stock AS stock, c.id AS categoryId, c.name AS categoryName, p.primary_image_url AS primaryImageUrl,
            EXISTS (
                SELECT 1
                FROM wishlist_item wi
                JOIN wishlist w ON wi.wishlist_id = w.id
                WHERE wi.product_id = p.id
                  AND w.user_id = :userId
            ) AS isInWishlist,
            EXISTS (
                SELECT 1
                FROM cart_item ci
                JOIN cart c ON ci.cart_id = c.id
                WHERE ci.product_id = p.id
                  AND c.user_id = :userId
            ) AS isInCart
            FROM product p
            JOIN seller s ON s.user_id = p.seller_user_id
            JOIN category c ON c.id = p.category_id
            WHERE p.status = :status
            """,
            countQuery = """
                SELECT COUNT(*)
                FROM product p
                WHERE p.status = :status
            """,
            nativeQuery = true)
    Page<BuyerProductCardProjection> getProductsWithWishlistAndCart(@Param("status") String status, @Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT
            p.id AS id, p.name AS name, s.user_id AS sellerId, s.full_name AS sellerName,
            p.description AS description, p.price AS price, p.stock AS stock, c.id AS categoryId,
            c.name AS categoryName, p.primary_image_url AS primaryImageUrl, p.image_urls AS imageUrls,
            COALESCE(
                    json_agg(
                        json_build_object(
                            'id', pa.id,
                            'name', pa.attribute_name,
                            'value', pa.attribute_value
                        )
                    ) FILTER (WHERE pa.id IS NOT NULL),
                    '[]'
                ) AS attributes,
            EXISTS (
                SELECT 1
                FROM wishlist_item wi
                JOIN wishlist w ON wi.wishlist_id = w.id
                WHERE wi.product_id = p.id
                  AND w.user_id = :userId
            ) AS isInWishlist,
            EXISTS (
                SELECT 1
                FROM cart_item ci
                JOIN cart c ON ci.cart_id = c.id
                WHERE ci.product_id = p.id
                  AND c.user_id = :userId
            ) AS isInCart
            FROM product p
            JOIN seller s ON s.user_id = p.seller_user_id
            JOIN category c ON c.id = p.category_id
            LEFT JOIN product_attribute pa ON pa.product_id = p.id
            WHERE p.id = :productId
              AND p.status = :status
            GROUP BY
                p.id, s.user_id, c.id""", nativeQuery = true)
    Optional<BuyerProductProjection> findProductWishlistAndCart(@Param("productId") Long productId, @Param("userId") Long userId, @Param("status") String status);
}
