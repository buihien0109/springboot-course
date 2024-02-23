package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.techmaster.ecommecerapp.model.dto.TransactionNormalDto;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "TransactionNormalDtoMapping",
                        classes = @ConstructorResult(
                                targetClass = TransactionNormalDto.class,
                                columns = {
                                        @ColumnResult(name = "id", type = Long.class),
                                        @ColumnResult(name = "transaction_date", type = String.class),
                                        @ColumnResult(name = "sender_name", type = String.class),
                                        @ColumnResult(name = "receiver_name", type = String.class),
                                        @ColumnResult(name = "supplier", type = String.class),
                                        @ColumnResult(name = "transaction_items", type = String.class)
                                }
                        )
                )
        }
)

@NamedNativeQuery(
        name = "getAllTransactions",
        resultSetMapping = "TransactionNormalDtoMapping",
        query = """
                SELECT t.id, t.transaction_date as transaction_date, t.sender_name, t.receiver_name,
                JSON_OBJECT('supplierId', s.supplier_id, 'name', s.name) as supplier,
                (SELECT JSON_ARRAYAGG(JSON_OBJECT('id', ti.id, 'quantity', ti.quantity, 'purchasePrice', ti.purchase_price, 'productId', ti.product_id, 'productName', p.name))
                FROM transaction_item ti
                JOIN product p ON ti.product_id = p.product_id
                WHERE ti.transaction_id = t.id) as transaction_items
                FROM transaction t
                JOIN supplier s ON t.supplier_id = s.supplier_id
                ORDER BY t.transaction_date DESC
                """
)

@NamedNativeQuery(
        name = "getAllTransactionsBySupplier",
        resultSetMapping = "TransactionNormalDtoMapping",
        query = """
                SELECT t.id, t.transaction_date as transaction_date, t.sender_name, t.receiver_name,
                JSON_OBJECT('supplierId', s.supplier_id, 'name', s.name) as supplier,
                (SELECT JSON_ARRAYAGG(JSON_OBJECT('id', ti.id, 'quantity', ti.quantity, 'purchasePrice', ti.purchase_price, 'productId', ti.product_id, 'productName', p.name))
                FROM transaction_item ti
                JOIN product p ON ti.product_id = p.product_id
                WHERE ti.transaction_id = t.id) as transaction_items
                FROM transaction t
                JOIN supplier s ON t.supplier_id = s.supplier_id
                WHERE s.supplier_id = ?1
                ORDER BY t.transaction_date DESC
                """
)

@NamedNativeQuery(
        name = "getTransactionById",
        resultSetMapping = "TransactionNormalDtoMapping",
        query = """
                SELECT t.id, t.transaction_date as transaction_date, t.sender_name, t.receiver_name,
                JSON_OBJECT('supplierId', s.supplier_id, 'name', s.name) as supplier,
                (SELECT JSON_ARRAYAGG(JSON_OBJECT('id', ti.id, 'quantity', ti.quantity, 'purchasePrice', ti.purchase_price, 'productId', ti.product_id, 'productName', p.name))
                FROM transaction_item ti
                JOIN product p ON ti.product_id = p.product_id
                WHERE ti.transaction_id = t.id) as transaction_items
                FROM transaction t
                JOIN supplier s ON t.supplier_id = s.supplier_id
                WHERE t.id = ?1
                """
)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "transaction_date")
    Date transactionDate;

    @Column(name = "sender_name")
    String senderName;

    @Column(name = "receiver_name")
    String receiverName;

    @Transient
    Integer totalAmount;

    public Integer getTotalAmount() {
        // calculate total amount
        int total = 0;
        for (TransactionItem item : transactionItems) {
            total += item.getQuantity() * item.getPurchasePrice();
        }
        return total;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<TransactionItem> transactionItems = new LinkedHashSet<>();
}
