package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_CategoryId(Long categoryId);

    // get all product has discount active
    Page<Product> findByDiscounts_Status(DiscountCampaign.Status status, Pageable pageable);

    // get all product has discount active and product status in
    Page<Product> findByDiscounts_StatusAndStatusIn(DiscountCampaign.Status status, List<Product.Status> productStatus, Pageable pageable);

    // get products list same category with product id and get by limit
    List<Product> findByCategory_CategoryIdAndProductIdNotAndStatusIn(Long categoryId, Long productId, List<Product.Status> status);

    // get all product by parent category name
    List<Product> findByCategory_ParentCategory_NameIgnoreCase(String name);

    // get all product by parent category slug ignore case and product status
    Page<Product> findByCategory_ParentCategory_SlugIgnoreCaseAndStatusIn(String slug, List<Product.Status> status, Pageable pageable);

    // get all product by parent category slug and category slug has pagination
    Page<Product> findByCategory_ParentCategory_SlugIgnoreCaseAndCategory_SlugIgnoreCaseAndStatusIn(String slug, String subSlug, List<Product.Status> status, Pageable pageable);

    Set<Product> findByProductIdIn(List<Long> productIds);
}