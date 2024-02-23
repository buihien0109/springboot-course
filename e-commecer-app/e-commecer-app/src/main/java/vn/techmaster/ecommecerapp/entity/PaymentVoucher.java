package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.techmaster.ecommecerapp.model.dto.PaymentVoucherDto;

import java.util.Date;

@SqlResultSetMapping(
        name = "PaymentVoucherDtoResultMapping",
        classes = @ConstructorResult(
                targetClass = PaymentVoucherDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "purpose", type = String.class),
                        @ColumnResult(name = "note", type = String.class),
                        @ColumnResult(name = "amount", type = Integer.class),
                        @ColumnResult(name = "user", type = String.class),
                        @ColumnResult(name = "createdAt", type = String.class),
                        @ColumnResult(name = "updatedAt", type = String.class)
                }
        )
)

@NamedNativeQuery(
        name = "getAllPaymentVouchersDtoInRangeTime",
        resultSetMapping = "PaymentVoucherDtoResultMapping",
        query = """
                SELECT pv.id, pv.purpose, pv.note, pv.amount, JSON_OBJECT('userId', u.user_id, 'username', u.username) as user, pv.created_at as createdAt, pv.updated_at as updatedAt
                FROM payment_voucher pv
                JOIN user u ON pv.user_id = u.user_id
                WHERE pv.created_at BETWEEN ?1 AND ?2
                ORDER BY pv.created_at DESC
                """
)

@NamedNativeQuery(
        name = "getAllPaymentVouchersDto",
        resultSetMapping = "PaymentVoucherDtoResultMapping",
        query = """
                SELECT pv.id, pv.purpose, pv.note, pv.amount, JSON_OBJECT('userId', u.user_id, 'username', u.username) as user, pv.created_at as createdAt, pv.updated_at as updatedAt
                FROM payment_voucher pv
                JOIN user u ON pv.user_id = u.user_id
                ORDER BY pv.created_at DESC
                """
)

@NamedNativeQuery(
        name = "getPaymentVoucherDtoById",
        resultSetMapping = "PaymentVoucherDtoResultMapping",
        query = """
                SELECT pv.id, pv.purpose, pv.note, pv.amount, JSON_OBJECT('userId', u.user_id, 'username', u.username) as user, pv.created_at as createdAt, pv.updated_at as updatedAt
                FROM payment_voucher pv
                JOIN user u ON pv.user_id = u.user_id
                WHERE pv.id = ?1
                ORDER BY pv.created_at DESC
                """
)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_voucher")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "TEXT")
    String purpose;

    @Column(columnDefinition = "TEXT")
    String note;

    Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    Date createdAt;
    Date updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }
}
