package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.model.ProductAdminDto;
import vn.techmaster.ecommecerapp.model.dto.ProductNormalAdminDto;
import vn.techmaster.ecommecerapp.model.dto.ProductNormalDto;
import vn.techmaster.ecommecerapp.model.projection.product.ProductPublic;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatusIn(List<Product.Status> status);

    List<Product> findByCategory_CategoryId(Long categoryId);

    // get all product has discount active
    Page<Product> findByDiscounts_Status(DiscountCampaign.Status status, Pageable pageable);

    // get all product has discount active and product status in
    Page<Product> findByDiscounts_StatusAndStatusIn(DiscountCampaign.Status status, List<Product.Status> productStatus, Pageable pageable);

    Page<Product> findByDiscounts_EndDateGreaterThanEqualAndStatusIn(Date discountsEndDate, List<Product.Status> status, Pageable pageable);

    // get products list same category with product id and get by limit
    List<Product> findByCategory_CategoryIdAndProductIdNotAndStatusIn(Long categoryId, Long productId, List<Product.Status> status);

    // get all product by parent category name
    List<Product> findByCategory_ParentCategory_NameIgnoreCase(String name);


    Page<Product> findByCategory_ParentCategory_SlugIgnoreCaseAndStatusIn(String slug, List<Product.Status> status, Pageable pageable);

    // get all product by parent category slug and category slug has pagination
    Page<Product> findByCategory_ParentCategory_SlugIgnoreCaseAndCategory_SlugIgnoreCaseAndStatusIn(String slug, String subSlug, List<Product.Status> status, Pageable pageable);

    Set<Product> findByProductIdIn(List<Long> productIds);

    List<Product> findByProductIdBetween(Long start, Long end);

    @Query(nativeQuery = true, value = "SELECT p.* FROM product p")
    List<Product> getAllProducts();

    @Query(nativeQuery = true, name = "getAllDiscountProducts")
    List<ProductNormalDto> getAllDiscountProducts();

    @Query(nativeQuery = true, name = "getProductsByParentCategorySlug")
    List<ProductNormalDto> getProductsByParentCategorySlug(String parentCategorySlug);

    @Query(nativeQuery = true, name = "getProductsByCategorySlug")
    List<ProductNormalDto> getProductsByCategorySlug(String categorySlug);

    @Query(nativeQuery = true, name = "getRelatedProducts")
    List<ProductNormalDto> getRelatedProducts(@Param("categoryId") Long categoryId, @Param("productId") Long productId, @Param("limit") int limit);

    @Query(nativeQuery = true, name = "getAllProductsAdmin")
    List<ProductNormalAdminDto> getAllProductsAdmin();

    @Query(nativeQuery = true, name = "getAllAvailabelProductsAdminDtoByAdmin")
    List<ProductAdminDto> getAllAvailabelProductsAdminDtoByAdmin();

    @Query(nativeQuery = true, name = "getAllAvailabelProductsNormalAdminDtoByAdmin")
    List<ProductNormalAdminDto> getAllAvailabelProductsNormalAdminDtoByAdmin();
}