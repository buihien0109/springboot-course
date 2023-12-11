package com.example.demo.thymeleaf.crud.service;

import com.example.demo.thymeleaf.crud.entity.Product;
import com.example.demo.thymeleaf.crud.entity.ProductReview;
import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.repository.ProductRepository;
import com.example.demo.thymeleaf.crud.repository.ProductReviewRepository;
import com.example.demo.thymeleaf.crud.request.ReviewData;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductRepository productRepository;
    private final ProductReviewRepository reviewRepository;
    private final HttpSession session;

    public List<ProductReview> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public void addReview(Long productId, ReviewData reviewData) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Cannot find product with id " + productId);
        }

        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            throw new RuntimeException("User is not logged in");
        }

        ProductReview review = new ProductReview();
        review.setContent(reviewData.getContent());
        review.setRating(reviewData.getRating());
        review.setProduct(product);
        review.setUser(user);
        reviewRepository.save(review);
    }
}
