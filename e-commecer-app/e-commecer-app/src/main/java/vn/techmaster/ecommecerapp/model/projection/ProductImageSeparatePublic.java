package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.ProductImage;

import java.util.List;

public interface ProductImageSeparatePublic {
    ProductImagePublic getMainImage();

    List<ProductImagePublic> getSubImages();

    @RequiredArgsConstructor
    class ProductImageSeparatePublicImpl implements ProductImageSeparatePublic {
        @JsonIgnore
        private final List<ProductImage> productImages;

        @Override
        public ProductImagePublic getMainImage() {
            ProductImage mainImage = productImages.stream()
                    .filter(image -> image.getImageType().equals(ProductImage.ImageType.MAIN)).findFirst().orElse(null);
            if (mainImage == null) {
                return null;
            }
            return ProductImagePublic.of(mainImage);
        }

        @Override
        public List<ProductImagePublic> getSubImages() {
            List<ProductImage> subImages = productImages.stream()
                    .filter(image -> image.getImageType().equals(ProductImage.ImageType.SUB)).toList();
            return subImages.stream().map(ProductImagePublic::of).toList();
        }
    }

    static ProductImageSeparatePublic of(List<ProductImage> productImages) {
        return new ProductImageSeparatePublicImpl(productImages);
    }
}
