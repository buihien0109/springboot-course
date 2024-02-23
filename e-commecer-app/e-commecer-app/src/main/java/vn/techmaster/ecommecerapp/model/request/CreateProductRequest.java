package vn.techmaster.ecommecerapp.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.techmaster.ecommecerapp.entity.Product;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateProductRequest {
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotEmpty(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer stockQuantity;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    private Integer price;

    @NotNull(message = "Trạng thái không được để trống")
    private Product.Status status;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;

    @NotNull(message = "Nhà cung cấp không được để trống")
    private Long supplierId;
}
