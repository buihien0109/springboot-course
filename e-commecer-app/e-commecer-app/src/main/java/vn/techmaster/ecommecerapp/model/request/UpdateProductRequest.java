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
    private String mainImageUrl;
    private List<String> subImageUrls;
    private List<AttributeRequest> attributes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class AttributeRequest {
        private String name;
        private String value;
    }
}
