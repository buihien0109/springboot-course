package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.Review;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.ReviewPublic;
import vn.techmaster.ecommecerapp.model.request.CreateReviewAnonymousRequest;
import vn.techmaster.ecommecerapp.model.request.UpsertReviewRequest;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.ReviewRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public List<ReviewPublic> getAllReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProduct_ProductIdOrderByUpdatedAtDesc(productId);

        // covert reviews to List<ReviewPublic> and sort items by updatedAt desc using stream
        return reviews.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .map(ReviewPublic::of)
                .toList();
    }

    public List<ReviewPublic> getAllReviewsAvailableByProductId(Long productId) {
        List<Review> reviews = reviewRepository
                .findByProduct_ProductIdAndStatusOrderByUpdatedAtDesc(productId, Review.Status.ACCEPTED);

        // covert reviews to List<ReviewPublic> and sort items by updatedAt desc using stream
        return reviews.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .map(ReviewPublic::of)
                .toList();
    }

    public List<ReviewPublic> getAllReviewsByProductIdByAdmin(Long productId) {
        List<Review> reviews = reviewRepository.getAllReviewsByProductId(productId);

        // covert reviews to List<ReviewPublic> and sort items by updatedAt desc using stream
        return reviews.stream()
                .map(ReviewPublic::of)
                .toList();
    }

    public ReviewPublic createReview(UpsertReviewRequest request) {
        // get user id from security context
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Không tìm thấy người dùng!");
        }

        // get product id from request
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm!"));

        // create review
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStatus(Review.Status.ACCEPTED);

        // save review
        reviewRepository.save(review);

        // return review
        return ReviewPublic.of(review);
    }

    public ReviewPublic createReviewAnonymous(CreateReviewAnonymousRequest request) {
        // get product id from request
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm!"));

        // create review
        Review review = new Review();
        review.setAuthorName(request.getAuthorName());
        review.setAuthorEmail(request.getAuthorEmail());
        review.setAuthorPhone(request.getAuthorPhone());
        review.setAuthorAvatar(generateLinkAuthorAvatar(request.getAuthorName()));
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStatus(Review.Status.PENDING);

        // save review
        reviewRepository.save(review);

        // return review
        return ReviewPublic.of(review);
    }

    // get character first each of word from string, and to uppercase
    public String getCharacter(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(word.charAt(0));
        }
        return result.toString().toUpperCase();
    }

    // generate link author avatar follow struct : https://placehold.co/200x200?text=[...]
    public String generateLinkAuthorAvatar(String authorName) {
        return "https://placehold.co/200x200?text=" + getCharacter(authorName);
    }

    public ReviewPublic updateReview(UpsertReviewRequest request, Long id) {
        // get user id from security context
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Không tìm thấy người dùng!");
        }

        // check user is owner of review
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá!"));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("Bạn không phải là chủ sở hữu của đánh giá này!");
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
            throw new BadRequestException("Không tìm thấy người dùng!");
        }

        // check user is owner of review
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá!"));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("Bạn không phải là chủ sở hữu của đánh giá này!");
        }

        // delete review
        reviewRepository.delete(review);
    }

    public ReviewPublic adminUpdateReview(UpsertReviewRequest request, Long id) {
        // get user id from security context
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Không tìm thấy người dùng!");
        }

        // check user is owner of review
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá!"));

        // update review
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // save review
        reviewRepository.save(review);

        // return review
        return ReviewPublic.of(review);
    }

    public void adminDeleteReview(Long id) {
        // get user id from security context
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Không tìm thấy người dùng!");
        }

        // find review by id
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá!"));

        // delete review
        reviewRepository.delete(review);
    }

    public ReviewPublic adminChangeStatusReview(Review.Status status, Long id) {
        // get user id from security context
        User user = SecurityUtils.getCurrentUserLogin();
        if (user == null) {
            throw new BadRequestException("Không tìm thấy người dùng!");
        }

        // find review by id
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy đánh giá!"));

        // check status is not PENDING
        if (review.getStatus() != Review.Status.PENDING) {
            throw new BadRequestException("Trạng thái đánh giá không hợp lệ!");
        }

        // update status
        if (status == Review.Status.ACCEPTED) {
            review.setStatus(Review.Status.ACCEPTED);
        } else if (status == Review.Status.REJECTED) {
            review.setStatus(Review.Status.REJECTED);
        } else {
            throw new BadRequestException("Trạng thái đánh giá không hợp lệ!");
        }

        // save review
        reviewRepository.save(review);

        // return review
        return ReviewPublic.of(review);
    }
}
