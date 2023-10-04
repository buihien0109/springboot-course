package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateTransactionRequest {
    private String senderName;
    private String receiverName;
    private Long supplierId;
    private Date transactionDate;
}
