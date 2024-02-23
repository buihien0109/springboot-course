package vn.techmaster.ecommecerapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.techmaster.ecommecerapp.model.dto.DiscountCampaignDto;
import vn.techmaster.ecommecerapp.model.dto.DiscountCampaignNormalDto;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "DiscountCampaignNormalDtoResultMapping",
                        classes = @ConstructorResult(
                                targetClass = DiscountCampaignNormalDto.class,
                                columns = {
                                        @ColumnResult(name = "campaign_id", type = Long.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "discount_type", type = String.class),
                                        @ColumnResult(name = "discount_value", type = Integer.class),
                                        @ColumnResult(name = "start_date", type = String.class),
                                        @ColumnResult(name = "end_date", type = String.class),
                                        @ColumnResult(name = "product_count", type = Integer.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "DiscountCampaignDtoResultMapping",
                        classes = @ConstructorResult(
                                targetClass = DiscountCampaignDto.class,
                                columns = {
                                        @ColumnResult(name = "campaign_id", type = Long.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "slug", type = String.class),
                                        @ColumnResult(name = "description", type = String.class),
                                        @ColumnResult(name = "discount_type", type = String.class),
                                        @ColumnResult(name = "discount_value", type = Integer.class),
                                        @ColumnResult(name = "start_date", type = String.class),
                                        @ColumnResult(name = "end_date", type = String.class),
                                        @ColumnResult(name = "products", type = String.class)
                                }
                        )
                )
        }
)

@NamedNativeQuery(
        name = "getAllDiscountCampaignsNormalDto",
        resultSetMapping = "DiscountCampaignNormalDtoResultMapping",
        query = """
                SELECT
                    dc.campaign_id,
                    dc.name,
                    dc.discount_type,
                    dc.discount_value,
                    dc.start_date,
                    dc.end_date,
                    COUNT(p.product_id) AS product_count
                FROM
                    discount_campaign dc
                    LEFT JOIN product_discount pd ON dc.campaign_id = pd.discount_id
                    LEFT JOIN product p ON pd.product_id = p.product_id
                GROUP BY
                    dc.campaign_id
                ORDER BY dc.campaign_id DESC
                """
)

@NamedNativeQuery(
        name = "getDiscountCampaignDtoById",
        resultSetMapping = "DiscountCampaignDtoResultMapping",
        query = """
                SELECT
                    dc.campaign_id,
                    dc.name,
                    dc.slug,
                    dc.description,
                    dc.discount_type,
                    dc.discount_value,
                    dc.start_date,
                    dc.end_date,
                    CASE
                        WHEN COUNT(p.product_id) > 0 THEN
                            JSON_ARRAYAGG(
                                JSON_OBJECT(
                                    'productId', p.product_id,
                                    'name', p.name,
                                    'price', p.price,
                                    'stockQuantity', p.stock_quantity,
                                    'status', p.status,
                                    'category', JSON_OBJECT(
                                        'categoryId', c.category_id,
                                        'name', c.name
                                    ),
                                    'supplier', JSON_OBJECT(
                                        'supplierId', s.supplier_id,
                                        'name', s.name
                                    )
                                )
                            )
                        ELSE
                            NULL
                    END AS products
                FROM
                    discount_campaign dc
                    LEFT JOIN product_discount pd ON dc.campaign_id = pd.discount_id
                    LEFT JOIN product p ON pd.product_id = p.product_id
                    LEFT JOIN category c ON p.category_id = c.category_id
                    LEFT JOIN supplier s ON p.supplier_id = s.supplier_id
                WHERE
                    dc.campaign_id = ?1
                GROUP BY
                    dc.campaign_id
                """
)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "discount_campaign")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long campaignId;

    String name;
    String slug;
    String description;

    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    Integer discountValue;
    Date startDate;
    Date endDate;

    @Transient
    Status status;

    @ManyToMany(mappedBy = "discounts", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    Set<Product> products = new LinkedHashSet<>();

    public void removeProduct(Product product) {
        products.remove(product);
        product.getDiscounts().remove(this);
    }

    public Status getStatus() {
        Date now = new Date();
        if (now.before(startDate)) {
            return Status.PENDING;
        } else if (now.after(endDate)) {
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
