package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.OrderItem;

import java.math.BigDecimal;

public interface OrderItemPublic {
    Long getOrderItemId();

    ProductPublic getProduct();

    Integer getQuantity();

    BigDecimal getPrice();

    @RequiredArgsConstructor
    class OrderItemPublicImpl implements OrderItemPublic {
        @JsonIgnore
        private final OrderItem orderItem;

        @Override
        public Long getOrderItemId() {
            return orderItem.getOrderItemId();
        }

        @Override
        public ProductPublic getProduct() {
            return ProductPublic.of(orderItem.getProduct());
        }

        @Override
        public Integer getQuantity() {
            return orderItem.getQuantity();
        }

        @Override
        public BigDecimal getPrice() {
            return orderItem.getPrice();
        }
    }

    static OrderItemPublic of(OrderItem orderItem) {
        return new OrderItemPublicImpl(orderItem);
    }
}
