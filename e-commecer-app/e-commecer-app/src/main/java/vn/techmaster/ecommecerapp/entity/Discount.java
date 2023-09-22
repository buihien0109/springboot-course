package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private Integer discountValue;
    private Date startDate;
    private Date endDate;

    @Transient
    private Integer remainingPrice;

    public Integer getRemainingPrice() {
        // calculate remaining price of product with discountType and discountValue
        if (discountType == DiscountType.PERCENT) {
            return product.getPrice() - (product.getPrice() * discountValue / 100);
        } else {
            return product.getPrice() - discountValue;
        }
    }

    public static enum DiscountType {
        PERCENT, AMOUNT
    }
}
