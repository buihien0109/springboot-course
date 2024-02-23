package vn.techmaster.ecommecerapp.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReviewAnonymousRequest {
    @NotEmpty(message = "Họ tên không được để trống")
    String authorName;

    @NotEmpty(message = "Email không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email không hợp lệ")
    String authorEmail;

    @NotEmpty(message = "Số điện thoại tác giả không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại không hợp lệ")
    String authorPhone;

    @NotNull(message = "Đánh giá không được để trống")
    @Min(value = 1, message = "Đánh giá phải từ 1 đến 5")
    Integer rating;

    @NotEmpty(message = "Bình luận không được để trống")
    @NotNull(message = "Bình luận không được để trống")
    String comment;

    @NotNull(message = "ID sản phẩm không được để trống")
    Long productId;
}
