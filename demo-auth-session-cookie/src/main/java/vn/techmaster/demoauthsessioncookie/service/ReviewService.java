package vn.techmaster.demoauthsessioncookie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.demoauthsessioncookie.entity.Review;
import vn.techmaster.demoauthsessioncookie.model.request.UpsertReviewRequest;
import vn.techmaster.demoauthsessioncookie.repository.ReviewRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> getReviewsOfMovie(Integer movieId) {
        return reviewRepository.findByMovie_IdOrderByCreatedAtDesc(movieId);
    }

    public Review createReview(UpsertReviewRequest request) {
        return null;
    }

    public Review updateReview(Integer id, UpsertReviewRequest request) {
        return null;
    }

    public void deleteReview(Integer id) {
    }
}
