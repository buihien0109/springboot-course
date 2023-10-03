package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.techmaster.ecommecerapp.entity.Discount;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.ProductImage;

import java.util.List;
import java.util.Set;

public interface ProductPublic {
    Long getProductId();

    String getName();

    String getSlug();

    String getDescription();

    Integer getPrice();

    Integer getStockQuantity();

    Product.Status getStatus();

    CategoryPublic getCategory();

    ProductImageSeparatePublic getImages();

    List<ProductAttributePublic> getAttributes();

    Integer getDiscountPrice();

    @RequiredArgsConstructor
    @Slf4j
    class ProductPublicImpl implements ProductPublic {
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
        public String getDescription() {
            return product.getDescription();
        }

        @Override
        public Integer getPrice() {
            return product.getPrice();
        }

        @Override
        public Integer getStockQuantity() {
            return product.getStockQuantity();
        }

        @Override
        public Product.Status getStatus() {
            return product.getStatus();
        }

        @Override
        public CategoryPublic getCategory() {
            return CategoryPublic.of(product.getCategory());
        }

        @Override
        public ProductImageSeparatePublic getImages() {
            List<ProductImage> productImages = product.getImages();
            return ProductImageSeparatePublic.of(productImages);
        }

        @Override
        public List<ProductAttributePublic> getAttributes() {
            return product.getAttributes().stream().map(ProductAttributePublic::of).toList();
        }

        @Override
        public Integer getDiscountPrice() {
            // check if product has discount and discount campaign is active
            Set<Discount> discounts = product.getDiscounts();

            if (!discounts.isEmpty()) {
                for (Discount discount : discounts) {
                    DiscountCampaign discountCampaign = discount.getDiscountCampaign();
                    if (discountCampaign.getStatus() == DiscountCampaign.Status.ACTIVE) {
                        // check if discount is valid
                        if (discount.getDiscountType() == Discount.DiscountType.PERCENT) {
                            return product.getPrice() * (100 - discount.getDiscountValue()) / 100;
                        } else if (discount.getDiscountType() == Discount.DiscountType.AMOUNT) {
                            return product.getPrice() - discount.getDiscountValue();
                        }
                    }
                }
            }
            return null;
        }
    }

    static ProductPublic of(Product product) {
        return new ProductPublicImpl(product);
    }
}
