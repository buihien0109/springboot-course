package vn.techmaster.ecommecerapp.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.Product}
 */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAdminDto implements Serializable {
    Long productId;
    String name;
    Integer price;
    Integer stockQuantity;
    Integer discountPrice;

    public ProductAdminDto(Long productId, String name, Integer price, Integer stockQuantity, Integer discountPrice) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.discountPrice = discountPrice;
    }
}