package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.techmaster.ecommecerapp.entity.Review;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.util.Date;

@ToString
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDto {
    Long reviewId;
    String comment;
    Integer rating;
    Review.Status status;
    Long authorId;
    String authorName;
    String authorAvatar;
    Date createdAt;
    Date updatedAt;

    public ReviewDto(Long reviewId, String comment, Integer rating, String status, Long authorId, String authorName, String authorAvatar, String createdAt, String updatedAt) {
        this.reviewId = reviewId;
        this.comment = comment;
        this.rating = rating;
        this.status = Review.Status.valueOf(status);
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.createdAt = DateUtils.parseDate(createdAt);
        this.updatedAt = DateUtils.parseDate(updatedAt);
    }
}
