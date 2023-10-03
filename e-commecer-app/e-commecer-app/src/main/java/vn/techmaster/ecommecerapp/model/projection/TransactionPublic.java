package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import vn.techmaster.ecommecerapp.entity.Transaction;

import java.util.Date;
import java.util.List;

public interface TransactionPublic {
    Long getId();

    Date getTransactionDate();

    Integer getTotalAmount();

    String getSenderName();

    String getReceiverName();

    List<TransactionItemPublic> getTransactionItems();

    SupplierPublic getSupplier();

    @RequiredArgsConstructor
    class TransactionPublicImpl implements TransactionPublic {
        @JsonIgnore
        private final Transaction transaction;

        @Override
        public Long getId() {
            return transaction.getId();
        }

        @Override
        public Date getTransactionDate() {
            return transaction.getTransactionDate();
        }

        @Override
        public Integer getTotalAmount() {
            return transaction.getTotalAmount();
        }

        @Override
        public String getSenderName() {
            return transaction.getSenderName();
        }

        @Override
        public String getReceiverName() {
            return transaction.getReceiverName();
        }

        @Override
        public List<TransactionItemPublic> getTransactionItems() {
            return transaction.getTransactionItems().stream()
                    .map(TransactionItemPublic::of)
                    .toList();
        }

        @Override
        public SupplierPublic getSupplier() {
            return SupplierPublic.of(transaction.getSupplier());
        }
    }

    static TransactionPublic of(Transaction transaction) {
        return new TransactionPublicImpl(transaction);
    }
}
