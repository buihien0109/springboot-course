package vn.techmaster.ecommecerapp.rest.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpsertReviewRequest;
import vn.techmaster.ecommecerapp.service.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewResources {

    private final ReviewService reviewService;

    public ReviewResources(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody UpsertReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@RequestBody UpsertReviewRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.updateReview(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().body("Review deleted successfully!");
    }
}
