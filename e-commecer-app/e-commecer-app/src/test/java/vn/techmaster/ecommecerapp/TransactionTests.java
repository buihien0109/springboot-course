package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.Supplier;
import vn.techmaster.ecommecerapp.entity.Transaction;
import vn.techmaster.ecommecerapp.entity.TransactionItem;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.SupplierRepository;
import vn.techmaster.ecommecerapp.repository.TransactionItemRepository;
import vn.techmaster.ecommecerapp.repository.TransactionRepository;

@SpringBootTest
public class TransactionTests {
    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void save_transaction() {
        // tạo 3 giao dịch với 3 nhà cung cấp khác nhau
        Supplier supplier1 = supplierRepository.findById(1L).get();
        Supplier supplier2 = supplierRepository.findById(2L).get();
        Supplier supplier3 = supplierRepository.findById(3L).get();

        // tạo 3 giao dịch với 3 nhà cung cấp khác nhau
        Transaction transaction1 = new Transaction();
        transaction1.setSenderName(supplier1.getName());
        transaction1.setTransactionDate(java.sql.Date.valueOf("2023-10-01"));
        transaction1.setReceiverName("Nguyễn Văn A");
        transaction1.setSupplier(supplier1);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setSenderName(supplier2.getName());
        transaction2.setTransactionDate(java.sql.Date.valueOf("2023-10-02"));
        transaction2.setReceiverName("Nguyễn Văn B");
        transaction2.setSupplier(supplier2);
        transactionRepository.save(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setSenderName(supplier3.getName());
        transaction3.setTransactionDate(java.sql.Date.valueOf("2023-09-24"));
        transaction3.setReceiverName("Nguyễn Văn C");
        transaction3.setSupplier(supplier3);
        transactionRepository.save(transaction3);
    }

    @Test
    void save_transaction_item_and_product() {
        Faker faker = new Faker();
        // Mỗi transaction có 3 transaction item
        // Mỗi transaction item có 5 product

        Transaction transaction1 = transactionRepository.findById(1L).get();
        Transaction transaction2 = transactionRepository.findById(2L).get();
        Transaction transaction3 = transactionRepository.findById(3L).get();

        // Tạo 3 transaction item cho transaction1
        for (int i = 1; i <= 3; i++) {
            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setTransaction(transaction1);

            // Tạo 5 product cho mỗi transaction item
            for (int j = 1; j <= 5; j++) {
                Product product = productRepository.findById((long) j).get();
                transactionItem.setProduct(product);
                transactionItem.setQuantity(faker.number().numberBetween(10, 20));
                transactionItem.setPurchasePrice(product.getPrice() - 1000);
            }
            transactionItemRepository.save(transactionItem);
        }

        for (int i = 1; i <= 3; i++) {
            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setTransaction(transaction2);

            // Tạo 5 product cho mỗi transaction item
            for (int j = 6; j <= 10; j++) {
                Product product = productRepository.findById((long) j).get();
                transactionItem.setProduct(product);
                transactionItem.setQuantity(faker.number().numberBetween(10, 20));
                transactionItem.setPurchasePrice(product.getPrice() - 1000);
            }
            transactionItemRepository.save(transactionItem);
        }

        for (int i = 1; i <= 3; i++) {
            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setTransaction(transaction3);

            // Tạo 5 product cho mỗi transaction item
            for (int j = 11; j <= 15; j++) {
                Product product = productRepository.findById((long) j).get();
                transactionItem.setProduct(product);
                transactionItem.setQuantity(faker.number().numberBetween(10, 20));
                transactionItem.setPurchasePrice(product.getPrice() - 1000);
            }
            transactionItemRepository.save(transactionItem);
        }
    }
}
