package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.ProductAttribute;

public interface ProductAttributePublic {
    Long getAttributeId();

    String getAttributeName();

    String getAttributeValue();

    @RequiredArgsConstructor
    class ProductAttributePublicImpl implements ProductAttributePublic {
        @JsonIgnore
        private final ProductAttribute productAttribute;

        @Override
        public Long getAttributeId() {
            return productAttribute.getAttributeId();
        }

        @Override
        public String getAttributeName() {
            return productAttribute.getAttributeName();
        }

        @Override
        public String getAttributeValue() {
            return productAttribute.getAttributeValue();
        }
    }

    static ProductAttributePublic of(ProductAttribute productAttribute) {
        return new ProductAttributePublicImpl(productAttribute);
    }
}
