package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateReviewAnonymousRequest {
    private String authorName;
    private String authorEmail;
    private String authorPhone;
    private Integer rating;
    private String comment;
    private Long productId;
}
