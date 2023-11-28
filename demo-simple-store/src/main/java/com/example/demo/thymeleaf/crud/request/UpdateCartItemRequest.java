package com.example.demo.thymeleaf.crud.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequest {
    @NotNull(message = "Mã sản phẩm không được để trống")
    private Long cartItemId;

    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;
}
