package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpsertTransactionItemRequest {
    private Long transactionId;
    private Long productId;
    private Integer quantity;
    private Integer purchasePrice;
}
