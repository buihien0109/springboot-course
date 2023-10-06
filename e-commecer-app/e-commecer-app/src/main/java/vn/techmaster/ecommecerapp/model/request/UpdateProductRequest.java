package vn.techmaster.ecommecerapp.model.request;


import lombok.*;
import vn.techmaster.ecommecerapp.entity.Product;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateProductRequest {
    private String name;
    private Integer price;
    private String description;
    private Product.Status status;
    private Long categoryId;
    private Long supplierId;
    private List<AttributeRequest> attributes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class AttributeRequest {
        private Long attributeId;
        private String attributeName;
        private String attributeValue;
    }
}
