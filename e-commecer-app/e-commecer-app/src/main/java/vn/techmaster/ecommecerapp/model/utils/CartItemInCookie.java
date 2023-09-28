package vn.techmaster.ecommecerapp.model.utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CartItemInCookie {
    private Long cartItemId;
    private Long productId;
    private Integer quantity;
}
