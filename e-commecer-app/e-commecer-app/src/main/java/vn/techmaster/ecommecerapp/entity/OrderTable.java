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

    public enum Status {
        WAIT, DELIVERY, COMPLETE, CANCELED, RETURNED
    }

    public enum PaymentMethod {
        COD, PAYPAL, STRIPE
    }

    public enum ShippingMethod {
        STANDARD, EXPRESS
    }

    public enum UseType {
        USER, ANONYMOUS
    }

    @PrePersist
    public void prePersist() {
        orderDate = new Date();
    }
}
