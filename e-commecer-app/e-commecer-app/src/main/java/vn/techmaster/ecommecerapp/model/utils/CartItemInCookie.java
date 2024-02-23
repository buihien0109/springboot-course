package vn.techmaster.ecommecerapp.model.utils;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemInCookie {
    Long cartItemId;
    Long productId;
    Integer quantity;
}
