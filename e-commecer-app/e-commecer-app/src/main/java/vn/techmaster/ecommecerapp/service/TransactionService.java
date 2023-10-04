package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.Supplier;
import vn.techmaster.ecommecerapp.entity.Transaction;
import vn.techmaster.ecommecerapp.entity.TransactionItem;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.TransactionPublic;
import vn.techmaster.ecommecerapp.model.request.CreateTransactionRequest;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;
import vn.techmaster.ecommecerapp.repository.TransactionRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final SupplierRepository supplierRepository;


    public List<TransactionPublic> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionPublic::of).toList();
    }

    public TransactionPublic getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy giao dịch với id: " + id));
        return TransactionPublic.of(transaction);
    }

    public TransactionPublic createTransaction(CreateTransactionRequest request) {
        // check if supplier exists
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy nhà cung cấp với id: " + request.getSupplierId()));

        // create transaction
        Transaction transaction = new Transaction();
        transaction.setSenderName(request.getSenderName());
        transaction.setReceiverName(request.getReceiverName());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setSupplier(supplier);

        // create transaction items
        request.getTransactionItems().forEach(item -> {
            // get product by id
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy sản phẩm với id: " + item.getProductId()));

            // create transaction item
            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setQuantity(item.getQuantity());
            transactionItem.setPurchasePrice(item.getPurchasePrice());
            transactionItem.setProduct(product);
            transactionItem.setTransaction(transaction);

            // add transaction item to transaction
            transaction.getTransactionItems().add(transactionItem);

            // update product quantity
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        });

        // save transaction
        transactionRepository.save(transaction);

        return TransactionPublic.of(transaction);
    }
}
