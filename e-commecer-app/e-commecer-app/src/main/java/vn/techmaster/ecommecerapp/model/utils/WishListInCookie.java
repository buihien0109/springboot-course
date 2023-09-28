package vn.techmaster.ecommecerapp.model.utils;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class WishListInCookie {
    private Long wishlistId;
    private Long productId;
}
