package vn.techmaster.ecommecerapp.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToCartRequest {
    @NotNull(message = "Product ID không được để trống")
    Long productId;

    @NotNull(message = "Số lượng không được để trống")
    Integer quantity;
}
