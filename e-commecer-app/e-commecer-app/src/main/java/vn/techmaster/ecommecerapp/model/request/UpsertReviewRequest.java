package vn.techmaster.ecommecerapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpsertReviewRequest {
    private Long productId;
    private Integer rating;
    private String comment;
}
