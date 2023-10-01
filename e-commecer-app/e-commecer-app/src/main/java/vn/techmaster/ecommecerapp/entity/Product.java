package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductAttribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Discount> discounts = new ArrayList<>();

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
