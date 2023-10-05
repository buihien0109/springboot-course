package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "discount_campaign")
public class DiscountCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    private String name;
    private String slug;
    private String description;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private Integer discountValue;

    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(mappedBy = "discounts")
    @Fetch(FetchMode.SUBSELECT)
    private Set<Product> products = new LinkedHashSet<>();

    @Getter
    public enum Status {
        PENDING("Chưa kích hoạt"),
        ACTIVE("Đang hoạt động"),
        EXPIRED("Đã hết hạn"),
        CANCEL("Hủy bỏ");

        private final String value;

        Status(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum DiscountType {
        PERCENT("Phần trăm"),
        AMOUNT("Số tiền"),
        SAME_PRICE("Đồng giá");

        private final String value;

        DiscountType(String value) {
            this.value = value;
        }
    }
}
