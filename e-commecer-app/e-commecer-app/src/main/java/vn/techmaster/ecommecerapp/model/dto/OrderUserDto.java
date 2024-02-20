package vn.techmaster.ecommecerapp.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.OrderTable}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderUserDto implements Serializable {
    Long orderId;
    String orderNumber;
    Date orderDate;
    OrderTable.Status status;
    List<OrderItemDto> orderItems = new ArrayList<>();
    Integer totalAmount;
    Integer couponDiscount;

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.OrderItem}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class OrderItemDto implements Serializable {
        Long id;
        Long productId;
        String productName;
        Integer quantity;
        Integer price;
    }

    public OrderUserDto(Long orderId, String orderNumber, String orderDate, String status, String orderItems, Integer couponDiscount) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderDate = DateUtils.parseDate(orderDate);
        this.status = OrderTable.Status.valueOf(status);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.orderItems = objectMapper.readValue((String) orderItems, new TypeReference<List<OrderUserDto.OrderItemDto>>() {
            });
        } catch (IOException e) {
            this.orderItems = new ArrayList<>();
        }

        this.couponDiscount = couponDiscount;
        int totalAmount = this.orderItems.stream().mapToInt(orderItemDto -> orderItemDto.getPrice() * orderItemDto.getQuantity()).sum();
        if (this.couponDiscount != null) {
            this.totalAmount = totalAmount - (totalAmount * this.couponDiscount / 100);
        } else {
            this.totalAmount = totalAmount;
        }
    }
}