package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.LinkedHashSet;
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

    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "discountCampaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Discount> discounts = new LinkedHashSet<>();

    @Getter
    public enum Status {
        PENDING("Chưa kích hoạt"),
        ACTIVE("Đang hoạt động"),
        EXPIRED("Đã hết hạn");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
