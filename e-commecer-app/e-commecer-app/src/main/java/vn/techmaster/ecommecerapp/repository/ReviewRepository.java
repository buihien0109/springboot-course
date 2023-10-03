package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_ProductIdOrderByUpdatedAtDesc(Long productId);

    @Query("select r from Review r where r.product.productId = ?1 order by r.createdAt DESC")
    List<Review> getAllReviewsByProductId(Long productId);



    List<Review> findByProduct_ProductId(Long productId);
}