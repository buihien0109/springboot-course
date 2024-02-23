package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "coupon")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long couponId;

    @Column(nullable = false, unique = true)
    String code;

    Integer discount;
    Date validFrom;
    Date validTo;

    @Transient
    Status status;

    // Generate status discount
    public Status getStatus() {
        Date now = new Date();
        if (now.before(validFrom)) {
            return Status.PENDING;
        } else if (now.after(validTo)) {
            return Status.EXPIRED;
        } else {
            return Status.ACTIVE;
        }
    }

    @Getter
    public enum Status {
        PENDING("Chưa kích hoạt"),
        ACTIVE("Đang hoạt động"),
        EXPIRED("Đã hết hạn");

        private final String value;

        Status(String value) {
            this.value = value;
        }
    }

    public Coupon(Long couponId, String code, Integer discount, Date validFrom, Date validTo) {
        this.couponId = couponId;
        this.code = code;
        this.discount = discount;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
}
