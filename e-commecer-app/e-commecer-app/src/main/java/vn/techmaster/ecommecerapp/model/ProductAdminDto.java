package vn.techmaster.ecommecerapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.Product}
 */
@NoArgsConstructor
@Getter
@Setter
public class ProductAdminDto implements Serializable {
    private Long productId;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private Integer discountPrice;

    public ProductAdminDto(Long productId, String name, Integer price, Integer stockQuantity, Integer discountPrice) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.discountPrice = discountPrice;
    }
}