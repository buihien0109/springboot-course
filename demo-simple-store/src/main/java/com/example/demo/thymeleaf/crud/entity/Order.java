package com.example.demo.thymeleaf.crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Tên không được để trống")
    private String name;

    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phone;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;

    private String note;

    private LocalDateTime orderDate;

    @NotNull(message = "Phương thức vận chuyển không được để trống")
    @Enumerated(EnumType.STRING)
    private ShippingMethod shippingMethod;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
        orderItem.setOrder(this);
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

    @PrePersist
    public void prePersist() {
        orderDate = LocalDateTime.now();
    }
}
