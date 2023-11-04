package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.TransactionItem;
import vn.techmaster.ecommecerapp.model.projection.product.ProductPublic;

public interface TransactionItemPublic {
    Long getId();

    Integer getQuantity();

    Integer getPurchasePrice();

    ProductPublic getProduct();

    @RequiredArgsConstructor
    class TransactionItemPublicImpl implements TransactionItemPublic {
        @JsonIgnore
        private final TransactionItem transactionItem;

        @Override
        public Long getId() {
            return transactionItem.getId();
        }

        @Override
        public Integer getQuantity() {
            return transactionItem.getQuantity();
        }

        @Override
        public Integer getPurchasePrice() {
            return transactionItem.getPurchasePrice();
        }

        @Override
        public ProductPublic getProduct() {
            return ProductPublic.of(transactionItem.getProduct());
        }
    }

    static TransactionItemPublic of(TransactionItem transactionItem) {
        return new TransactionItemPublicImpl(transactionItem);
    }
}
