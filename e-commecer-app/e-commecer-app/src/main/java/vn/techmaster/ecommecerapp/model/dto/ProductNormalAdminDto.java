package vn.techmaster.ecommecerapp.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.techmaster.ecommecerapp.entity.Product;

import java.io.IOException;
import java.io.Serializable;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.Product}
 */
@NoArgsConstructor
@Getter
@Setter
public class ProductNormalAdminDto implements Serializable {
    private Long productId;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private Product.Status status;
    private CategoryDto category;
    private SupplierDto supplier;

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.Category}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CategoryDto implements Serializable {
        private Long categoryId;
        private String name;
    }

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.Supplier}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class SupplierDto implements Serializable {
        private Long supplierId;
        private String name;
    }

    public ProductNormalAdminDto(Long productId, String name, Integer price, Integer stockQuantity, String status, String category, String supplier) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = Product.Status.valueOf(status);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.category = objectMapper.readValue((String) category, ProductNormalAdminDto.CategoryDto.class);
        } catch (IOException e) {
            this.category = null;
        }

        try {
            this.supplier = objectMapper.readValue((String) supplier, ProductNormalAdminDto.SupplierDto.class);
        } catch (IOException e) {
            this.category = null;
        }
    }
}