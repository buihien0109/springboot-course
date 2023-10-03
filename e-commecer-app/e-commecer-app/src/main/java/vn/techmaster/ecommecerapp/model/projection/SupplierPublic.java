package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Supplier;

public interface SupplierPublic {
    Long getSupplierId();
    String getName();
    String getAddress();
    String getEmail();
    String getPhone();
    String getThumbnail();

    @RequiredArgsConstructor
    class SupplierPublicImpl implements SupplierPublic {
        @JsonIgnore
        private final Supplier supplier;

        @Override
        public Long getSupplierId() {
            return supplier.getSupplierId();
        }

        @Override
        public String getName() {
            return supplier.getName();
        }

        @Override
        public String getAddress() {
            return supplier.getAddress();
        }

        @Override
        public String getEmail() {
            return supplier.getEmail();
        }

        @Override
        public String getPhone() {
            return supplier.getPhone();
        }

        @Override
        public String getThumbnail() {
            return supplier.getThumbnail();
        }
    }

    static SupplierPublic of(Supplier supplier) {
        return new SupplierPublicImpl(supplier);
    }
}
