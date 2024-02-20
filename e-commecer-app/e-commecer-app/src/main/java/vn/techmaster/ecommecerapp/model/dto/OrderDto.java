package vn.techmaster.ecommecerapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link OrderTable}
 */
@NoArgsConstructor
@Getter
@Setter
public class OrderDto implements Serializable {
    private Long orderId;
    private String orderNumber;
    private Date orderDate;
    private String username;
    private String phone;
    private String email;
    private OrderTable.Status status;
    private Long userId;

    public OrderDto(Long orderId, String orderNumber, String orderDate, String username, String phone, String email, String status, Long userId) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderDate = DateUtils.parseDate(orderDate);
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.status = OrderTable.Status.valueOf(status);
        this.userId = userId;
    }
}