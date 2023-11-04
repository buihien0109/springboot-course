package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.WishList;
import vn.techmaster.ecommecerapp.model.projection.product.ProductPublic;

public interface WishListPublic {
    Long getWishlistId();

    ProductPublic getProduct();

    @RequiredArgsConstructor
    class WishListPublicImpl implements WishListPublic {
        @JsonIgnore
        private final WishList wishList;

        @Override
        public Long getWishlistId() {
            return wishList.getWishlistId();
        }

        @Override
        public ProductPublic getProduct() {
            return ProductPublic.of(wishList.getProduct());
        }
    }

    static WishListPublic of(WishList wishList) {
        return new WishListPublicImpl(wishList);
    }
}
