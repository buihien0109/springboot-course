package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
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
    private BigDecimal totalAmount;

    @Transient
    private BigDecimal temporaryAmount;

    @Transient
    private BigDecimal discountAmount;

    public BigDecimal getTotalAmount() {
        return this.getTemporaryAmount().subtract(this.getDiscountAmount());
    }

    public BigDecimal getTemporaryAmount() {
        return this.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity()))).
                reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDiscountAmount() {
        if(this.getCouponCode() == null || this.getCouponCode().isBlank()) {
            return BigDecimal.ZERO;
        }
        return this.getTemporaryAmount().multiply(new BigDecimal(this.getCouponDiscount())).divide(new BigDecimal(100));
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

    public static enum Status {
        WAIT, DELIVERY, COMPLETE, CANCELED, RETURNED
    }

    public static enum PaymentMethod {
        COD, PAYPAL, STRIPE
    }

    public static enum ShippingMethod {
        STANDARD, EXPRESS
    }

    public static enum UseType {
        USER, ANONYMOUS
    }

    @PrePersist
    public void prePersist() {
        orderDate = new Date();
    }
}
