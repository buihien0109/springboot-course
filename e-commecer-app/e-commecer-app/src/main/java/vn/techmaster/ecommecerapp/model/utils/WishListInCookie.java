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
public class WishListInCookie {
    Long wishlistId;
    Long productId;
}
