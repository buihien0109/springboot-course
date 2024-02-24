package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.techmaster.ecommecerapp.model.dto.OrderDto;
import vn.techmaster.ecommecerapp.model.dto.OrderUserDetailDto;
import vn.techmaster.ecommecerapp.model.dto.OrderUserDto;
import vn.techmaster.ecommecerapp.model.dto.RevenueDto;

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
                @SqlResultSetMapping(
                        name = "RevenueDtoResultMapping",
                        classes = @ConstructorResult(
                                targetClass = RevenueDto.class,
                                columns = {
                                        @ColumnResult(name = "month", type = Integer.class),
                                        @ColumnResult(name = "year", type = Integer.class),
                                        @ColumnResult(name = "revenue", type = Long.class)
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
        name = "getRevenueByMonth",
        resultSetMapping = "RevenueDtoResultMapping",
        query = """
                    SELECT
                        MONTH(sub.order_date) AS month,
                        YEAR(sub.order_date) AS year,
                        SUM(sub.total) AS revenue
                    FROM (
                        SELECT
                            o.order_date,
                            CASE
                                WHEN o.coupon_code IS NOT NULL and o.coupon_discount  > 0 THEN SUM(oi.quantity * oi.price * (1 - o.coupon_discount / 100))
                                ELSE SUM(oi.quantity * oi.price)
                            END AS total
                        FROM
                            order_table o
                        JOIN
                            order_item oi ON o.order_id = oi.order_id
                        WHERE
                            o.status = 'COMPLETE'
                        GROUP BY
                            o.order_id, o.order_date, o.coupon_code, o.coupon_discount
                        ) AS sub
                    GROUP BY
                        MONTH(sub.order_date), YEAR(sub.order_date)
                    ORDER BY
                        YEAR(sub.order_date), MONTH(sub.order_date)
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderId;

    String orderNumber;
    Date orderDate;

    String username;
    String phone;
    String email;

    String province;
    String district;
    String ward;
    String address;

    @Column(columnDefinition = "TEXT")
    String note;

    @Column(columnDefinition = "TEXT")
    String adminNote;

    @Enumerated(EnumType.STRING)
    ShippingMethod shippingMethod;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    UseType useType;

    String couponCode;
    Integer couponDiscount;

    @Transient
    Integer totalAmount;

    @Transient
    Integer temporaryAmount;

    @Transient
    Integer discountAmount;

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
    User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    List<OrderItem> orderItems = new ArrayList<>();

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
