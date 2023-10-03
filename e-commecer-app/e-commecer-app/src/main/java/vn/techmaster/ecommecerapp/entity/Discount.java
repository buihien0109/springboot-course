package vn.techmaster.ecommecerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private Integer discountValue;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "discount_campaign_id")
    private DiscountCampaign discountCampaign;

    @ManyToMany(mappedBy = "discounts")
    @Fetch(FetchMode.SUBSELECT)
    private Set<Product> products = new LinkedHashSet<>();

    @Getter
    public enum DiscountType {
        PERCENT("Phần trăm"),
        AMOUNT("Số tiền");

        private final String displayValue;

        DiscountType(String displayValue) {
            this.displayValue = displayValue;
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Discount discount = (Discount) o;
        return getDiscountId() != null && Objects.equals(getDiscountId(), discount.getDiscountId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
