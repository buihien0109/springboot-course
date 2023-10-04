package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.Transaction;
import vn.techmaster.ecommecerapp.entity.TransactionItem;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.TransactionItemPublic;
import vn.techmaster.ecommecerapp.model.request.UpsertTransactionItemRequest;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.TransactionItemRepository;
import vn.techmaster.ecommecerapp.repository.TransactionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionItemService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final TransactionItemRepository transactionItemRepository;

    public TransactionItemPublic createTransactionItem(UpsertTransactionItemRequest request) {
        // find transaction by id
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy transaction với id: " + request.getTransactionId()));

        // find product by id
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy product với id: " + request.getProductId()));

        // check if quantity and purchasePrice are valid
        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Số lượng phải lớn hơn 0");
        }

        if (request.getPurchasePrice() <= 0) {
            throw new BadRequestException("Giá phải lớn hơn 0");
        }

        // create new TransactionItem
        TransactionItem transactionItem = new TransactionItem();
        transactionItem.setTransaction(transaction);
        transactionItem.setProduct(product);
        transactionItem.setQuantity(request.getQuantity());
        transactionItem.setPurchasePrice(request.getPurchasePrice());

        // increase total product in stock
        product.setStockQuantity(product.getStockQuantity() + request.getQuantity());

        // save transactionItem
        transactionItemRepository.save(transactionItem);

        // save product
        productRepository.save(product);

        // return TransactionItemPublic
        return TransactionItemPublic.of(transactionItem);
    }

    public TransactionItemPublic updateTransactionItem(Long id, UpsertTransactionItemRequest request) {
        // find transactionItem by id
        TransactionItem transactionItem = transactionItemRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy transactionItem với id: " + id));

        // find transaction by id
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy transaction với id: " + request.getTransactionId()));

        // find product by id
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy product với id: " + request.getProductId()));

        // check if quantity and purchasePrice are valid
        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Số lượng phải lớn hơn 0");
        }

        if (request.getPurchasePrice() <= 0) {
            throw new BadRequestException("Giá phải lớn hơn 0");
        }

        // calculate difference between old quantity and new quantity
        int quantityDifference = request.getQuantity() - transactionItem.getQuantity();

        // update transactionItem
        transactionItem.setTransaction(transaction);
        transactionItem.setProduct(product);
        transactionItem.setQuantity(request.getQuantity());
        transactionItem.setPurchasePrice(request.getPurchasePrice());

        // increase total product in stock
        product.setStockQuantity(product.getStockQuantity() + quantityDifference);

        // save transactionItem
        transactionItemRepository.save(transactionItem);

        // save product
        productRepository.save(product);

        // return TransactionItemPublic
        return TransactionItemPublic.of(transactionItem);
    }

    public void deleteTransactionItem(Long id) {
        // find transactionItem by id
        TransactionItem transactionItem = transactionItemRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy transactionItem với id: " + id));

        // decrease total product in stock
        Product product = transactionItem.getProduct();
        product.setStockQuantity(product.getStockQuantity() - transactionItem.getQuantity());

        // save product
        productRepository.save(product);

        // delete transactionItem
        transactionItemRepository.delete(transactionItem);
    }
}
