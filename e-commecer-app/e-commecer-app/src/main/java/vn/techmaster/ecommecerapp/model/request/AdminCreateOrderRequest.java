package vn.techmaster.ecommecerapp.model.request;

import lombok.*;
import vn.techmaster.ecommecerapp.entity.OrderTable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminCreateOrderRequest {
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
    private List<OrderItemRequest> items;
}
