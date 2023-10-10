package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminUpsertOrderItemRequest {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Integer price;
}
