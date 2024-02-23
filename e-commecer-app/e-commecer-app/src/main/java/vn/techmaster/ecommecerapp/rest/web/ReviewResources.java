package vn.techmaster.ecommecerapp.rest.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.entity.Review;
import vn.techmaster.ecommecerapp.model.request.CreateReviewAnonymousRequest;
import vn.techmaster.ecommecerapp.model.request.UpsertReviewRequest;
import vn.techmaster.ecommecerapp.service.ReviewService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewResources {
    private final ReviewService reviewService;

    @PostMapping("/public/reviews")
    public ResponseEntity<?> createReview(@Valid @RequestBody UpsertReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @PostMapping("/public/reviews/anonymous")
    public ResponseEntity<?> createReviewAnonymous(@Valid @RequestBody CreateReviewAnonymousRequest request) {
        return ResponseEntity.ok(reviewService.createReviewAnonymous(request));
    }

    @PutMapping("/public/reviews/{id}")
    public ResponseEntity<?> updateReview(@Valid @RequestBody UpsertReviewRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.updateReview(request, id));
    }

    @DeleteMapping("/public/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().body("Xóa thành công");
    }

    @PutMapping("/admin/reviews/{id}")
    public ResponseEntity<?> adminUpdateReview(@Valid @RequestBody UpsertReviewRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.adminUpdateReview(request, id));
    }

    @PutMapping("/admin/reviews/{id}/update-status")
    public ResponseEntity<?> adminChangeStatusReview(@RequestParam Review.Status status, @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.adminChangeStatusReview(status, id));
    }

    @DeleteMapping("/admin/reviews/{id}")
    public ResponseEntity<?> adminDeleteReview(@PathVariable Long id) {
        reviewService.adminDeleteReview(id);
        return ResponseEntity.ok().body("Xóa thành công");
    }
}
