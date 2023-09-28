package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Cart;

import java.util.ArrayList;
import java.util.List;

public interface CartPublic {
    List<CartItemPublic> getCartItems();

    @RequiredArgsConstructor
    class CartPublicImpl implements CartPublic {
        @JsonIgnore
        private final Cart cart;

        @Override
        public List<CartItemPublic> getCartItems() {
            if (cart == null) {
                return new ArrayList<>();
            }
            return cart.getCartItems().stream().map(CartItemPublic::of).toList();
        }
    }

    static CartPublic of(Cart cart) {
        return new CartPublicImpl(cart);
    }
}
