package vn.techmaster.ecommecerapp.model.projection.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.techmaster.ecommecerapp.entity.DiscountCampaign;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.model.projection.CategoryPublic;
import vn.techmaster.ecommecerapp.model.projection.ProductAttributePublic;
import vn.techmaster.ecommecerapp.model.projection.SupplierPublic;

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

    String getMainImage();

    List<String> getSubImages();

    List<ProductAttributePublic> getAttributes();

    Integer getDiscountPrice();

    SupplierPublic getSupplier();

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
        public String getMainImage() {
            return product.getMainImage();
        }

        @Override
        public List<String> getSubImages() {
            return product.getSubImages();
        }

        @Override
        public List<ProductAttributePublic> getAttributes() {
            return product.getAttributes().stream().map(ProductAttributePublic::of).toList();
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

        @Override
        public SupplierPublic getSupplier() {
            if (product.getSupplier() == null) {
                return null;
            }
            return SupplierPublic.of(product.getSupplier());
        }
    }

    static ProductPublic of(Product product) {
        return new ProductPublicImpl(product);
    }
}
