package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.OrderTable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderTablePublic {
    Long getOrderId();

    String getOrderNumber();

    Date getOrderDate();

    String getUsername();

    String getPhone();

    String getEmail();

    String getProvince();

    String getDistrict();

    String getWard();

    String getAddress();

    String getNote();

    OrderTable.ShippingMethod getShippingMethod();

    OrderTable.PaymentMethod getPaymentMethod();

    OrderTable.Status getStatus();

    OrderTable.UseType getUseType();

    String getCouponCode();

    Integer getCouponDiscount();

    Integer getTotalAmount();

    Integer getTemporaryAmount();

    Integer getDiscountAmount();

    UserPublic getUser();

    List<OrderItemPublic> getOrderItems();

    @RequiredArgsConstructor
    class OrderTablePublicImpl implements OrderTablePublic {
        @JsonIgnore
        private final OrderTable orderTable;

        @Override
        public Long getOrderId() {
            return orderTable.getOrderId();
        }

        @Override
        public String getOrderNumber() {
            return orderTable.getOrderNumber();
        }

        @Override
        public Date getOrderDate() {
            return orderTable.getOrderDate();
        }

        @Override
        public String getUsername() {
            return orderTable.getUsername();
        }

        @Override
        public String getPhone() {
            return orderTable.getPhone();
        }

        @Override
        public String getEmail() {
            return orderTable.getEmail();
        }

        @Override
        public String getProvince() {
            return orderTable.getProvince();
        }

        @Override
        public String getDistrict() {
            return orderTable.getDistrict();
        }

        @Override
        public String getWard() {
            return orderTable.getWard();
        }

        @Override
        public String getAddress() {
            return orderTable.getAddress();
        }

        @Override
        public String getNote() {
            return orderTable.getNote();
        }

        @Override
        public OrderTable.ShippingMethod getShippingMethod() {
            return orderTable.getShippingMethod();
        }

        @Override
        public OrderTable.PaymentMethod getPaymentMethod() {
            return orderTable.getPaymentMethod();
        }

        @Override
        public OrderTable.Status getStatus() {
            return orderTable.getStatus();
        }

        @Override
        public OrderTable.UseType getUseType() {
            return orderTable.getUseType();
        }

        @Override
        public String getCouponCode() {
            return orderTable.getCouponCode();
        }

        @Override
        public Integer getCouponDiscount() {
            return orderTable.getCouponDiscount();
        }

        @Override
        public Integer getTotalAmount() {
            return orderTable.getTotalAmount();
        }

        @Override
        public Integer getTemporaryAmount() {
            return orderTable.getTemporaryAmount();
        }

        @Override
        public Integer getDiscountAmount() {
            return orderTable.getDiscountAmount();
        }

        @Override
        public UserPublic getUser() {
            if (orderTable.getUser() != null) {
                return UserPublic.of(orderTable.getUser());
            }
            return null;
        }

        @Override
        public List<OrderItemPublic> getOrderItems() {
            return orderTable.getOrderItems().stream().map(OrderItemPublic::of).toList();
        }
    }

    static OrderTablePublic of(OrderTable orderTable) {
        return new OrderTablePublicImpl(orderTable);
    }
}
