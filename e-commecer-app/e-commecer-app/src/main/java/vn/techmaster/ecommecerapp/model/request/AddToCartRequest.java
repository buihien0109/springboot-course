package vn.techmaster.ecommecerapp.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    @NotNull(message = "Product id is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}
