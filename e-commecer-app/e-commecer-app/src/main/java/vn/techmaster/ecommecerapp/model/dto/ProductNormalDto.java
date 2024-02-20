package vn.techmaster.ecommecerapp.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.techmaster.ecommecerapp.entity.Product;

@ToString
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductNormalDto {
    Long productId;
    String name;
    String slug;
    Integer price;
    Product.Status status;
    String imageUrl;
    Integer discountPrice;

    public ProductNormalDto(Long productId, String name, String slug, Integer price, String status, String imageUrl, Integer discountPrice) {
        this.productId = productId;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.status = Product.Status.valueOf(status);
        this.imageUrl = imageUrl;
        this.discountPrice = discountPrice;
    }
}
