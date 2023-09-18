package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}