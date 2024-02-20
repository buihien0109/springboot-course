package vn.techmaster.demoauthsessioncookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.demoauthsessioncookie.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByMovie_IdOrderByCreatedAtDesc(Integer movieId);
}