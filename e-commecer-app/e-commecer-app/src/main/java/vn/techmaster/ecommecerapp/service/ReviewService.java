package vn.techmaster.ecommecerapp.service;

import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.Review;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.model.projection.ReviewPublic;
import vn.techmaster.ecommecerapp.model.request.UpsertReviewRequest;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.ReviewRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public List<ReviewPublic> getAllReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProduct_ProductIdOrderByUpdatedAtDesc(productId);

        // covert reviews to List<ReviewPublic> and sort items by updatedAt desc using stream
        return reviews.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .map(ReviewPublic::of)
                .toList();
    }

    public ReviewPublic createReview(UpsertReviewRequest request) {
        // get user id from security context
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("User not found!");
        }

        // get product id from request
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        // create review
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // save review
        reviewRepository.save(review);

        // return review
        return ReviewPublic.of(review);
    }

    public ReviewPublic updateReview(UpsertReviewRequest request, Long id) {
        // get user id from security context
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("User not found!");
        }

        // check user is owner of review
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found!"));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("You are not owner of this review!");
        }

        // update review
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // save review
        reviewRepository.save(review);

        // return review
        return ReviewPublic.of(review);
    }

    public void deleteReview(Long id) {
        // get user id from security context
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("User not found!");
        }

        // check user is owner of review
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found!"));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("You are not owner of this review!");
        }

        // delete review
        reviewRepository.delete(review);
    }
}
