package vn.techmaster.ecommecerapp.model.projection.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.ProductImage;

import java.util.List;
import java.util.Set;

public interface ProductNormalPublic {
    Long getProductId();

    String getName();

    String getSlug();

    Integer getPrice();

    Product.Status getStatus();

    String getImageUrl();

    Integer getDiscountPrice();

    @RequiredArgsConstructor
    @Slf4j
    class ProductNormalPublicImpl implements ProductNormalPublic {
        @JsonIgnore
        private final Product product;

        @Override
        public Long getProductId() {
            return product.getProductId();
        }

        @Override
        public String getName() {
            return product.getName();
        }

        @Override
        public String getSlug() {
            return product.getSlug();
        }

        @Override
        public Integer getPrice() {
            return product.getPrice();
        }

        @Override
        public Product.Status getStatus() {
            return product.getStatus();
        }

        @Override
        public String getImageUrl() {
            List<ProductImage> productImages = product.getImages();
            ProductImage mainImage = productImages.stream()
                    .filter(image -> image.getImageType().equals(ProductImage.ImageType.MAIN)).findFirst().orElse(null);
            if (mainImage == null) {
                return null;
            }
            return mainImage.getImageUrl();
        }

        @Override
        public Integer getDiscountPrice() {
            // check if product has discount and discount campaign is active
            Set<DiscountCampaign> discounts = product.getDiscounts();

            if (!discounts.isEmpty()) {
                for (DiscountCampaign discount : discounts) {
                    if (discount.getStatus() == DiscountCampaign.Status.ACTIVE) {
                        if (discount.getDiscountType() == DiscountCampaign.DiscountType.PERCENT) {
                            return product.getPrice() * (100 - discount.getDiscountValue()) / 100;
                        } else if (discount.getDiscountType() == DiscountCampaign.DiscountType.AMOUNT) {
                            return product.getPrice() - discount.getDiscountValue();
                        } else if (discount.getDiscountType() == DiscountCampaign.DiscountType.SAME_PRICE) {
                            return discount.getDiscountValue();
                        }
                    }
                }
            }
            return null;
        }
    }

    static ProductNormalPublic of(Product product) {
        return new ProductNormalPublicImpl(product);
    }
}
