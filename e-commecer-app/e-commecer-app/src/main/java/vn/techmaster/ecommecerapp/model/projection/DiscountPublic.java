package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Discount;

import java.util.Date;

public interface DiscountPublic {
    Long getDiscountId();

    Discount.DiscountType getDiscountType();

    Integer getDiscountValue();

    Date getStartDate();

    Date getEndDate();

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
        public Date getStartDate() {
            return discount.getStartDate();
        }

        @Override
        public Date getEndDate() {
            return discount.getEndDate();
        }
    }

    static DiscountPublic of(Discount discount) {
        return new DiscountPublicImpl(discount);
    }
}
