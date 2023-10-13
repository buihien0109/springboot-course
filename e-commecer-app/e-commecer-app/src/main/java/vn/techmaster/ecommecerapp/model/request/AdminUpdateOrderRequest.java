package vn.techmaster.ecommecerapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.techmaster.ecommecerapp.entity.OrderTable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateOrderRequest {
    private Long userId;
    private String username;
    private String phone;
    private String email;
    private String province;
    private String district;
    private String ward;
    private String address;
    private String note;
    private OrderTable.ShippingMethod shippingMethod;
    private OrderTable.PaymentMethod paymentMethod;
    private String couponCode;
    private Integer couponDiscount;
}
