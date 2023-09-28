package vn.techmaster.ecommecerapp.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddToCartRequest {
    @NotNull(message = "Product id is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}
