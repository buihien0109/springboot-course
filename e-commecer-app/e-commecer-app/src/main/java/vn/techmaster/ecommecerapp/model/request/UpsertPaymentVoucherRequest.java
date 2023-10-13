package vn.techmaster.ecommecerapp.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpsertPaymentVoucherRequest {
    private String purpose;
    private String note;
    private Integer amount;
}
