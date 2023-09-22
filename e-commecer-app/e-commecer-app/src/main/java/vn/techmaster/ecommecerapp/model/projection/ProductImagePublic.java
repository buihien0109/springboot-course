package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.ProductImage;

public interface ProductImagePublic {
    Long getImageId();

    String getImageUrl();

    ProductImage.ImageType getImageType();

    @RequiredArgsConstructor
    class ProductImagePublicImpl implements ProductImagePublic {
        @JsonIgnore
        public final ProductImage productImage;

        @Override
        public Long getImageId() {
            return productImage.getImageId();
        }

        @Override
        public String getImageUrl() {
            return productImage.getImageUrl();
        }

        @Override
        public ProductImage.ImageType getImageType() {
            return productImage.getImageType();
        }
    }

    static ProductImagePublic of(ProductImage productImage) {
        return new ProductImagePublicImpl(productImage);
    }
}
