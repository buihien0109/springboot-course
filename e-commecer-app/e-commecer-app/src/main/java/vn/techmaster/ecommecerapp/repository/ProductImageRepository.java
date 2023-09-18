package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}