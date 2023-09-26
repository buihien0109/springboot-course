package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemRequest {
    Long productId;
    Integer quantity;
    private BigDecimal price;
}
