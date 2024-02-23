package vn.techmaster.ecommecerapp.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertReviewRequest {
    @NotNull(message = "ID sản phẩm không được để trống")
    Long productId;

    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải từ 1 đến 5")
    Integer rating;

    @NotEmpty(message = "Bình luận không được để trống")
    @NotNull(message = "Bình luận không được để trống")
    String comment;
}
