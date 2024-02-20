package vn.techmaster.ecommecerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import vn.techmaster.ecommecerapp.model.ProductAdminDto;
import vn.techmaster.ecommecerapp.model.dto.ProductNormalAdminDto;
import vn.techmaster.ecommecerapp.model.dto.ProductNormalDto;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "ProductNormalMapping",
                        classes = @ConstructorResult(
                                targetClass = ProductNormalDto.class,
                                columns = {
                                        @ColumnResult(name = "product_id", type = Long.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "slug", type = String.class),
                                        @ColumnResult(name = "price", type = Integer.class),
                                        @ColumnResult(name = "status", type = String.class),
                                        @ColumnResult(name = "main_image", type = String.class),
                                        @ColumnResult(name = "discount_price", type = Integer.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "ProductNormalAdminMapping",
                        classes = @ConstructorResult(
                                targetClass = ProductNormalAdminDto.class,
                                columns = {
                                        @ColumnResult(name = "product_id", type = Long.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "price", type = Integer.class),
                                        @ColumnResult(name = "stock_quantity", type = Integer.class),
                                        @ColumnResult(name = "status", type = String.class),
                                        @ColumnResult(name = "category", type = String.class),
                                        @ColumnResult(name = "supplier", type = String.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "ProductAdminDtoMapping",
                        classes = @ConstructorResult(
                                targetClass = ProductAdminDto.class,
                                columns = {
                                        @ColumnResult(name = "product_id", type = Long.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "price", type = Integer.class),
                                        @ColumnResult(name = "stock_quantity", type = Integer.class),
                                        @ColumnResult(name = "discount_price", type = Integer.class)
                                }
                        )
                ),
        }

)

@NamedNativeQuery(
        name = "getAllDiscountProducts",
        resultSetMapping = "ProductNormalMapping",
        query = """
                SELECT p.product_id as product_id, p.name, p.slug, p.price, p.status, p.main_image as main_image,
                        CASE dc.discount_type\s
                            WHEN 'PERCENT' THEN p.price - (p.price * dc.discount_value / 100)
                            WHEN 'AMOUNT' THEN p.price - dc.discount_value\s
                            ELSE dc.discount_value\s
                        END as discount_price
                FROM product p
                JOIN product_discount pd ON p.product_id = pd.product_id
                JOIN discount_campaign dc ON pd.discount_id = dc.campaign_id
                AND p.status IN ('AVAILABLE') and dc.end_date >= CURDATE()
                ORDER BY p.product_id
                """
)

@NamedNativeQuery(
        name = "getProductsByParentCategorySlug",
        resultSetMapping = "ProductNormalMapping",
        query = """
                SELECT p.product_id as product_id, p.name, p.slug, p.price, p.status, p.main_image as main_image,
                    CASE
                        WHEN dc.discount_type = 'PERCENT' THEN p.price - (p.price * dc.discount_value / 100)
                        WHEN dc.discount_type = 'AMOUNT' THEN p.price - dc.discount_value
                        WHEN dc.discount_type = 'SAME_PRICE' THEN dc.discount_value
                        ELSE NULL
                    END AS discount_price
                 FROM product p
                 LEFT JOIN product_discount pd ON p.product_id = pd.product_id
                 LEFT JOIN discount_campaign dc ON pd.discount_id = dc.campaign_id
                 WHERE p.status IN ('AVAILABLE')
                 AND p.category_id IN (SELECT category_id FROM category WHERE parent_category_id
                             IN (SELECT category_id FROM category WHERE slug = ?1))
                 AND (dc.end_date >= CURDATE() OR dc.end_date IS NULL)
                 ORDER BY p.product_id
                """
)

@NamedNativeQuery(
        name = "getProductsByCategorySlug",
        resultSetMapping = "ProductNormalMapping",
        query = """
                SELECT p.product_id as product_id, p.name, p.slug, p.price, p.status, p.main_image as main_image,
                    CASE
                        WHEN dc.discount_type = 'PERCENT' THEN p.price - (p.price * dc.discount_value / 100)
                        WHEN dc.discount_type = 'AMOUNT' THEN p.price - dc.discount_value
                        WHEN dc.discount_type = 'SAME_PRICE' THEN dc.discount_value
                        ELSE NULL
                    END AS discount_price
                 FROM product p
                 LEFT JOIN product_discount pd ON p.product_id = pd.product_id
                 LEFT JOIN discount_campaign dc ON pd.discount_id = dc.campaign_id
                 WHERE p.status IN ('AVAILABLE')
                 AND p.category_id IN (SELECT category_id FROM category WHERE slug = ?1)
                 AND (dc.end_date >= CURDATE() OR dc.end_date IS NULL)
                 ORDER BY p.product_id
                """
)

@NamedNativeQuery(
        name = "getRelatedProducts",
        resultSetMapping = "ProductNormalMapping",
        query = """
                SELECT p.product_id as product_id, p.name, p.slug, p.price, p.status, p.main_image as main_image,
                    CASE
                        WHEN dc.discount_type = 'PERCENT' THEN p.price - (p.price * dc.discount_value / 100)
                        WHEN dc.discount_type = 'AMOUNT' THEN p.price - dc.discount_value
                        WHEN dc.discount_type = 'SAME_PRICE' THEN dc.discount_value
                        ELSE NULL
                    END AS discount_price
                 FROM product p
                 LEFT JOIN product_discount pd ON p.product_id = pd.product_id
                 LEFT JOIN discount_campaign dc ON pd.discount_id = dc.campaign_id
                 WHERE p.status IN ('AVAILABLE')
                 AND p.category_id = :categoryId AND p.product_id != :productId
                 AND (dc.end_date >= CURDATE() OR dc.end_date IS NULL)
                 ORDER BY p.product_id
                 LIMIT :limit
                """
)

@NamedNativeQuery(
        name = "getAllProductsAdmin",
        resultSetMapping = "ProductNormalAdminMapping",
        query = """
                SELECT p.product_id as product_id, p.name, p.price, p.stock_quantity, p.status,
                    JSON_OBJECT('categoryId', c.category_id, 'name', c.name) as category,
                    JSON_OBJECT('supplierId', s.supplier_id, 'name', s.name) as supplier
                FROM product p
                LEFT JOIN category c ON p.category_id = c.category_id
                LEFT JOIN supplier s ON p.supplier_id = s.supplier_id
                ORDER BY p.product_id DESC
                """
)

@NamedNativeQuery(
        name = "getAllAvailabelProductsNormalAdminDtoByAdmin",
        resultSetMapping = "ProductNormalAdminMapping",
        query = """
                SELECT p.product_id as product_id, p.name, p.price, p.stock_quantity, p.status,
                    JSON_OBJECT('categoryId', c.category_id, 'name', c.name) as category,
                    JSON_OBJECT('supplierId', s.supplier_id, 'name', s.name) as supplier
                FROM product p
                LEFT JOIN category c ON p.category_id = c.category_id
                LEFT JOIN supplier s ON p.supplier_id = s.supplier_id
                WHERE p.status IN ('AVAILABLE')
                ORDER BY p.product_id DESC
                """
)

@NamedNativeQuery(
        name = "getAllAvailabelProductsAdminDtoByAdmin",
        resultSetMapping = "ProductAdminDtoMapping",
        query = """
                SELECT p.product_id as product_id, p.name, p.price, p.stock_quantity,
                    CASE
                        WHEN dc.discount_type = 'PERCENT' THEN p.price - (p.price * dc.discount_value / 100)
                        WHEN dc.discount_type = 'AMOUNT' THEN p.price - dc.discount_value
                        WHEN dc.discount_type = 'SAME_PRICE' THEN dc.discount_value
                        ELSE NULL
                    END AS discount_price
                 FROM product p
                 LEFT JOIN product_discount pd ON p.product_id = pd.product_id
                 LEFT JOIN discount_campaign dc ON pd.discount_id = dc.campaign_id
                 WHERE p.status IN ('AVAILABLE')
                 AND (dc.end_date >= CURDATE() OR dc.end_date IS NULL)
                 ORDER BY p.product_id
                """
)

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

    private String mainImage;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<String> subImages = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
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
