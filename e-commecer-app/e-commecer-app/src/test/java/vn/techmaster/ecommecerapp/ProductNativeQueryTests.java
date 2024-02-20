package vn.techmaster.ecommecerapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Review;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.ReviewRepository;
import vn.techmaster.ecommecerapp.repository.TransactionRepository;

@Slf4j
@SpringBootTest
public class ProductNativeQueryTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @Test
    void getAllDiscountProducts_test() {
        productRepository.getAllDiscountProducts().forEach(System.out::println);
    }

    @Test
    void getProductsByParentCategorySlug_test() {
        productRepository.getProductsByParentCategorySlug("sua-cac-loai").forEach(System.out::println);
    }

    @Test
    void getReviewsByProduct_test() {
        reviewRepository.getAllReviewsAvailableByProductId(6L, Review.Status.ACCEPTED.toString()).forEach(System.out::println);
    }

    @Test
    void getAllTransactionsBySupplier_test() {
        transactionRepository.getAllTransactionsBySupplier(1L).forEach(System.out::println);
    }
}
