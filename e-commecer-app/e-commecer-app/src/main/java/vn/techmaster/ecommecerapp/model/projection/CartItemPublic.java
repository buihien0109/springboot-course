package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.CartItem;
import vn.techmaster.ecommecerapp.model.projection.product.ProductPublic;

public interface CartItemPublic {
    Long getCartItemId();

    ProductPublic getProduct();

    Integer getQuantity();

    @RequiredArgsConstructor
    class CartItemPublicImpl implements CartItemPublic {
        @JsonIgnore
        private final CartItem cartItem;

        @Override
        public Long getCartItemId() {
            return cartItem.getCartItemId();
        }

        @Override
        public ProductPublic getProduct() {
            return ProductPublic.of(cartItem.getProduct());
        }

        @Override
        public Integer getQuantity() {
            return cartItem.getQuantity();
        }
    }

    static CartItemPublic of(CartItem cartItem) {
        return new CartItemPublicImpl(cartItem);
    }
}