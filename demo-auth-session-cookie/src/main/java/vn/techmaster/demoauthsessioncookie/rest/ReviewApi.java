package vn.techmaster.demoauthsessioncookie.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.demoauthsessioncookie.entity.Review;
import vn.techmaster.demoauthsessioncookie.model.request.UpsertReviewRequest;
import vn.techmaster.demoauthsessioncookie.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewApi {
    private final ReviewService reviewService;

    // Tạo review - POST.
    // Client -> Request chứa thông tin -> Server
    // Server đọc dữ liệu từ Request -> Xử lý -> Trả về kết quả cho Client
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody UpsertReviewRequest request) {
        Review review = reviewService.createReview(request);
        return ResponseEntity.ok(review); // status code 200
    }

    // Cập nhật review - PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@RequestBody UpsertReviewRequest request, @PathVariable Integer id) {
        Review review = reviewService.updateReview(id, request);
        return ResponseEntity.ok(review); // status code 200
    }

    // Xóa review - DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
