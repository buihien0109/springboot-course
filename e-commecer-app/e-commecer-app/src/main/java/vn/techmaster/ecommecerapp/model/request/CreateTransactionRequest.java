package vn.techmaster.ecommecerapp.model.request;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateTransactionRequest {
    private String senderName;
    private String receiverName;
    private Long supplierId;
    private Date transactionDate;
    private List<TransactionItemRequest> transactionItems;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class TransactionItemRequest {
        private Long productId;
        private Integer quantity;
        private Integer purchasePrice;
    }
}
