package vn.techmaster.ecommecerapp.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * DTO for {@link vn.techmaster.ecommecerapp.entity.Transaction}
 */
@ToString
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionNormalDto implements Serializable {
    Long id;
    Date transactionDate;
    String senderName;
    String receiverName;
    SupplierDto supplier;
    Set<TransactionItemDto> transactionItems = new LinkedHashSet<>();
    Integer totalAmount;

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.Supplier}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SupplierDto implements Serializable {
        Long supplierId;
        String name;
    }

    /**
     * DTO for {@link vn.techmaster.ecommecerapp.entity.TransactionItem}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TransactionItemDto implements Serializable {
        Long id;
        Integer quantity;
        Integer purchasePrice;
        Long productId;
        String productName;
    }

    public TransactionNormalDto(Long id, String transactionDate, String senderName, String receiverName, String supplier, String transactionItems) {
        this.id = id;
        this.transactionDate = DateUtils.parseDate(transactionDate);
        this.senderName = senderName;
        this.receiverName = receiverName;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.supplier = objectMapper.readValue((String) supplier, TransactionNormalDto.SupplierDto.class);
        } catch (IOException e) {
            this.supplier = null;
        }

        try {
            this.transactionItems = objectMapper.readValue((String) transactionItems, new TypeReference<Set<TransactionNormalDto.TransactionItemDto>>() {});
        } catch (IOException e) {
            this.transactionItems = new HashSet<>();
        }

        if (this.transactionItems != null || !this.transactionItems.isEmpty()) {
            this.totalAmount = this.transactionItems.stream().mapToInt(item -> item.getQuantity() * item.getPurchasePrice()).sum();
        }
    }
}