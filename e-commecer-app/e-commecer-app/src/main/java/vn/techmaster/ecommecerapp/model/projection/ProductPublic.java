package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.techmaster.ecommecerapp.entity.Discount;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.ProductImage;

import java.util.List;

public interface ProductPublic {
    Long getProductId();

    String getName();

    String getDescription();

    Integer getPrice();

    Integer getStockQuantity();

    Product.Status getStatus();

    CategoryPublic getCategory();

    ProductImageSeparatePublic getImages();

    List<ProductAttributePublic> getAttributes();

    DiscountPublic getDiscounts();

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
        public DiscountPublic getDiscounts() {
            // find discount not expired
            Discount discount = product.getDiscounts().stream().filter(d -> d.getEndDate().getTime() > System.currentTimeMillis()).findFirst().orElse(null);
            if (discount == null) {
                return null;
            }
            return DiscountPublic.of(discount);
        }
    }

    static ProductPublic of(Product product) {
        return new ProductPublicImpl(product);
    }
}
