package vn.techmaster.ecommecerapp.model.request;

import lombok.*;
import vn.techmaster.ecommecerapp.entity.Product;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateProductRequest {
    private String name;
    private Integer price;
    private String description;
    private Product.Status status;
    private Long categoryId;
}
