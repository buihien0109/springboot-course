package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.techmaster.ecommecerapp.entity.Review;
import vn.techmaster.ecommecerapp.model.dto.ReviewDto;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_ProductIdOrderByUpdatedAtDesc(Long productId);

    List<Review> findByProduct_ProductIdAndStatusOrderByUpdatedAtDesc(Long productId, Review.Status status);

    @Query(nativeQuery = true, name = "getAllReviewsByProductId")
    List<ReviewDto> getAllReviewsByProductId(Long productId);

    List<Review> findByProduct_ProductId(Long productId);

    @Query(nativeQuery = true, name = "getAllReviewsAvailableByProductId")
    List<ReviewDto> getAllReviewsAvailableByProductId(Long productId, String status);
}