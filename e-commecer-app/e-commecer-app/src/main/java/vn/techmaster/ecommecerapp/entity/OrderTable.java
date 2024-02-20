package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.techmaster.ecommecerapp.model.dto.OrderDto;
import vn.techmaster.ecommecerapp.model.dto.OrderUserDetailDto;
import vn.techmaster.ecommecerapp.model.dto.OrderUserDto;
import vn.techmaster.ecommecerapp.model.dto.ReviewDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "OrderUserDtoResultMapping",
                        classes = @ConstructorResult(
                                targetClass = OrderUserDto.class,
                                columns = {
                                        @ColumnResult(name = "order_id", type = Long.class),
                                        @ColumnResult(name = "order_number", type = String.class),
                                        @ColumnResult(name = "order_date", type = String.class),
                                        @ColumnResult(name = "status", type = String.class),
                                        @ColumnResult(name = "order_items", type = String.class),
                                        @ColumnResult(name = "coupon_discount", type = Integer.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "OrderDtoResultMapping",
                        classes = @ConstructorResult(
                                targetClass = OrderDto.class,
                                columns = {
                                        @ColumnResult(name = "order_id", type = Long.class),
                                        @ColumnResult(name = "order_number", type = String.class),
                                        @ColumnResult(name = "order_date", type = String.class),
                                        @ColumnResult(name = "username", type = String.class),
                                        @ColumnResult(name = "email", type = String.class),
                                        @ColumnResult(name = "phone", type = String.class),
                                        @ColumnResult(name = "status", type = String.class),
                                        @ColumnResult(name = "user_id", type = Long.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "OrderUserDetailDtoResultMapping",
                        classes = @ConstructorResult(
                                targetClass = OrderUserDetailDto.class,
                                columns = {
                                        @ColumnResult(name = "order_id", type = Long.class),
                                        @ColumnResult(name = "order_number", type = String.class),
                                        @ColumnResult(name = "order_date", type = String.class),
                                        @ColumnResult(name = "status", type = String.class),
                                        @ColumnResult(name = "order_items", type = String.class),
                                        @ColumnResult(name = "coupon_discount", type = Integer.class),
                                        @ColumnResult(name = "username", type = String.class),
                                        @ColumnResult(name = "phone", type = String.class),
                                        @ColumnResult(name = "email", type = String.class)
                                }
                        )
                ),

        }

)

@NamedNativeQuery(
        name = "getAllOrdersByUser",
        resultSetMapping = "OrderUserDtoResultMapping",
        query = """
                SELECT
                    o.order_id AS order_id,
                    o.order_number AS order_number,
                    o.order_date AS order_date,
                    o.status AS status,
                    o.coupon_discount AS coupon_discount,
                    JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', oi.order_item_id,
                            'productId', p.product_id,
                            'productName', p.name,
                            'quantity', oi.quantity,
                            'price', oi.price
                        )
                    ) AS order_items
                FROM
                    order_table o
                    JOIN order_item oi ON o.order_id = oi.order_id
                    JOIN product p ON oi.product_id = p.product_id
                WHERE
                    o.user_id = :userId
                GROUP BY
                    o.order_id, o.order_number, o.order_date, o.status
                ORDER BY
                    o.order_date DESC
                """
)

@NamedNativeQuery(
        name = "getAllOrdersInRangeTime",
        resultSetMapping = "OrderUserDtoResultMapping",
        query = """
                SELECT
                    o.order_id AS order_id,
                    o.order_number AS order_number,
                    o.order_date AS order_date,
                    o.status AS status,
                    o.coupon_discount AS coupon_discount,
                    JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', oi.order_item_id,
                            'productId', p.product_id,
                            'productName', p.name,
                            'quantity', oi.quantity,
                            'price', oi.price
                        )
                    ) AS order_items
                FROM
                    order_table o
                    JOIN order_item oi ON o.order_id = oi.order_id
                    JOIN product p ON oi.product_id = p.product_id
                WHERE
                    o.order_date BETWEEN ?1 AND ?2
                GROUP BY
                    o.order_id, o.order_number, o.order_date, o.status
                ORDER BY
                    o.order_date DESC
                """
)

@NamedNativeQuery(
        name = "getAllOrdersInRangeTimeByStatus",
        resultSetMapping = "OrderUserDetailDtoResultMapping",
        query = """
                SELECT
                    o.order_id AS order_id,
                    o.order_number AS order_number,
                    o.order_date AS order_date,
                    o.status AS status,
                    o.coupon_discount AS coupon_discount,
                    JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', oi.order_item_id,
                            'productId', p.product_id,
                            'productName', p.name,
                            'quantity', oi.quantity,
                            'price', oi.price
                        )
                    ) AS order_items,
                    o.username AS username,
                    o.email AS email,
                    o.phone AS phone
                FROM
                    order_table o
                    JOIN order_item oi ON o.order_id = oi.order_id
                    JOIN product p ON oi.product_id = p.product_id
                WHERE
                    o.order_date BETWEEN ?1 AND ?2
                    AND o.status = ?3
                GROUP BY
                    o.order_id, o.order_number, o.order_date, o.status
                ORDER BY
                    o.order_date DESC
                """
)

@NamedNativeQuery(
        name = "getAllOrdersDtoAdmin",
        resultSetMapping = "OrderDtoResultMapping",
        query = """
                SELECT
                    o.order_id AS order_id,
                    o.order_number AS order_number,
                    o.order_date AS order_date,
                    o.username AS username,
                    o.email AS email,
                    o.phone AS phone,
                    o.status AS status,
                    o.user_id AS user_id
                FROM
                    order_table o
                ORDER BY
                    o.order_date DESC
                """
)

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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
