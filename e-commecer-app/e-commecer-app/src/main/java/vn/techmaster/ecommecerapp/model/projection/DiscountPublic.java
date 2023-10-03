package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Discount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface DiscountPublic {
    Long getDiscountId();

    Discount.DiscountType getDiscountType();

    Integer getDiscountValue();

    List<ProductPublic> getProducts();

    @RequiredArgsConstructor
    class DiscountPublicImpl implements DiscountPublic {
        @JsonIgnore
        private final Discount discount;

        @Override
        public Long getDiscountId() {
            return discount.getDiscountId();
        }

        @Override
        public Discount.DiscountType getDiscountType() {
            return discount.getDiscountType();
        }

        @Override
        public Integer getDiscountValue() {
            return discount.getDiscountValue();
        }

        @Override
        public List<ProductPublic> getProducts() {
            if (discount.getProducts() == null) {
                return new ArrayList<>();
            }
            return discount.getProducts().stream().map(ProductPublic::of).toList();
        }
    }

    static DiscountPublic of(Discount discount) {
        return new DiscountPublicImpl(discount);
    }
}
