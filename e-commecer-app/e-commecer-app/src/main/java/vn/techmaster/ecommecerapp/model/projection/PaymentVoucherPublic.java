package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.PaymentVoucher;

import java.util.Date;

public interface PaymentVoucherPublic {
    Long getId();

    String getPurpose();

    String getNote();

    Integer getAmount();

    UserPublic getUser();

    Date getCreatedAt();

    Date getUpdatedAt();

    @RequiredArgsConstructor
    class PaymentVoucherPublicImpl implements PaymentVoucherPublic {
        @JsonIgnore
        private final PaymentVoucher paymentVoucher;

        @Override
        public Long getId() {
            return paymentVoucher.getId();
        }

        @Override
        public String getPurpose() {
            return paymentVoucher.getPurpose();
        }

        @Override
        public String getNote() {
            return paymentVoucher.getNote();
        }

        @Override
        public Integer getAmount() {
            return paymentVoucher.getAmount();
        }

        @Override
        public UserPublic getUser() {
            if (paymentVoucher.getUser() == null) {
                return null;
            }
            return UserPublic.of(paymentVoucher.getUser());
        }

        @Override
        public Date getCreatedAt() {
            return paymentVoucher.getCreatedAt();
        }

        @Override
        public Date getUpdatedAt() {
            return paymentVoucher.getUpdatedAt();
        }
    }

    static PaymentVoucherPublic of(PaymentVoucher paymentVoucher) {
        return new PaymentVoucherPublicImpl(paymentVoucher);
    }
}
