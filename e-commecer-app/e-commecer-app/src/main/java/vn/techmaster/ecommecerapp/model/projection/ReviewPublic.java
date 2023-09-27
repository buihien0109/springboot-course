package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Review;

import java.util.Date;

public interface ReviewPublic {
    Long getReviewId();

    String getComment();

    Integer getRating();

    Long getAuthorId();

    String getAuthorName();

    String getAuthorAvatar();

    Date getCreatedAt();

    Date getUpdatedAt();

    @RequiredArgsConstructor
    class ReviewPublicImpl implements ReviewPublic {
        @JsonIgnore
        private final Review review;

        @Override
        public Long getReviewId() {
            return review.getReviewId();
        }

        @Override
        public String getComment() {
            return review.getComment();
        }

        @Override
        public Integer getRating() {
            return review.getRating();
        }

        @Override
        public Long getAuthorId() {
            return review.getUser().getUserId();
        }

        @Override
        public String getAuthorName() {
            return review.getUser().getUsername();
        }

        @Override
        public String getAuthorAvatar() {
            return review.getUser().getAvatar();
        }

        @Override
        public Date getCreatedAt() {
            return review.getCreatedAt();
        }

        @Override
        public Date getUpdatedAt() {
            return review.getUpdatedAt();
        }
    }

    static ReviewPublic of(Review review) {
        return new ReviewPublicImpl(review);
    }
}
