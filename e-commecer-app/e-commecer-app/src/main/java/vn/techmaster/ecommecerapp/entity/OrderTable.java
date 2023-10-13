package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String orderNumber;
    private Date orderDate;

    private String username;
    private String phone;
    private String email;

    private String province;
    private String district;
    private String ward;
    private String address;
    private String note;

    @Enumerated(EnumType.STRING)
    private ShippingMethod shippingMethod;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private UseType useType;

    private String couponCode;
    private Integer couponDiscount;

    @Transient
    private Integer totalAmount;

    @Transient
    private Integer temporaryAmount;

    @Transient
    private Integer discountAmount;

    public Integer getTotalAmount() {
        return this.getTemporaryAmount() - this.getDiscountAmount();
    }

    public Integer getTemporaryAmount() {
        return this.getOrderItems()
                .stream()
                .mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }

    public Integer getDiscountAmount() {
        if (this.getCouponCode() == null || this.getCouponCode().isBlank()) {
            return 0;
        }
        return this.getTemporaryAmount() * this.getCouponDiscount() / 100;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    @Getter
    public enum Status {
        WAIT("Chờ xác nhận"),
        WAIT_DELIVERY("Chờ giao hàng"),
        DELIVERY("Đang giao"),
        COMPLETE("Đã giao"),
        CANCELED("Đã hủy"),
        RETURNED("Trả hàng");

        private final String displayValue;

        Status(String displayValue) {
            this.displayValue = displayValue;
        }
    }

    @Getter
    public enum PaymentMethod {
        COD("Thanh toán khi nhận hàng"),
        E_PAYMENT("Thanh toán trực tuyến");

        private final String displayValue;

        PaymentMethod(String displayValue) {
            this.displayValue = displayValue;
        }
    }

    @Getter
    public enum ShippingMethod {
        STANDARD("Ship tiêu chuẩn"), EXPRESS("Ship nhanh");

        private final String displayValue;

        ShippingMethod(String displayValue) {
            this.displayValue = displayValue;
        }
    }

    @Getter
    public enum UseType {
        USER("Khách hàng"), ANONYMOUS("Khách vãng lai");

        private final String displayValue;

        UseType(String displayValue) {
            this.displayValue = displayValue;
        }
    }

    @PrePersist
    public void prePersist() {
        orderDate = new Date();
    }
}
