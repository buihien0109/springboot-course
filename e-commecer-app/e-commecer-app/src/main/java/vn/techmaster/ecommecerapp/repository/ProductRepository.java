package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.model.projection.ProductPublic;

import java.util.Date;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_CategoryId(Long categoryId);

    // get all product has discount active
    Page<Product> findByDiscounts_DiscountCampaign_Status(DiscountCampaign.Status status, Pageable pageable);

    // get products list same category with product id and get by limit
    List<Product> findByCategory_CategoryIdAndProductIdNot(Long categoryId, Long productId);

    // get all product by category name
    List<Product> findByCategory_NameIgnoreCase(String name);

    // get all product by parent category name
    List<Product> findByCategory_ParentCategory_NameIgnoreCase(String name);

    // get all product by parent category slug
    List<Product> findByCategory_ParentCategory_SlugIgnoreCase(String slug);

    // get all product by parent category slug has pagination
    Page<Product> findByCategory_ParentCategory_SlugIgnoreCase(String slug, Pageable pageable);

    // get all product by parent category slug and category slug has pagination
    Page<Product> findByCategory_ParentCategory_SlugIgnoreCaseAndCategory_SlugIgnoreCase(String slug, String subSlug, Pageable pageable);
}