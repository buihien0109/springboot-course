package com.example.demo.thymeleaf.crud.repository;

import com.example.demo.thymeleaf.crud.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProductId(Long productId);
}