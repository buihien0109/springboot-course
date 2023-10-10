package vn.techmaster.ecommecerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer price;
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductAttribute> attributes = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_discount",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    @Fetch(FetchMode.SUBSELECT)
    private Set<DiscountCampaign> discounts = new LinkedHashSet<>();

    public void addDiscount(DiscountCampaign discountCampaign) {
        discounts.add(discountCampaign);
        discountCampaign.getProducts().add(this);
    }

    public void removeDiscount(DiscountCampaign discountCampaign) {
        discounts.remove(discountCampaign);
        discountCampaign.getProducts().remove(this);
    }

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Getter
    public enum Status {
        NOT_YET_SOLD("Chưa mở bán"),
        AVAILABLE("Đang bán"),
        UNAVAILABLE("Hết hàng"),
        CEASE("Ngừng bán");

        private final String displayValue;

        Status(String displayValue) {
            this.displayValue = displayValue;
        }
    }

    public void addAttribute(ProductAttribute attribute) {
        attributes.add(attribute);
        attribute.setProduct(this);
    }
}
