package vn.techmaster.ecommecerapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Discount;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.repository.DiscountRepository;
import vn.techmaster.ecommecerapp.repository.ProductRepository;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
public class DiscountTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountRepository discountRepository;

    @Test
    void save_discounts() {
        // Create 6 discounts with 3 discount for type percent and 3 discount for type amount
        for (int i = 1; i <= 3; i++) {
            // find product follow index + 1
            Product product = productRepository.findById(i + 1L).get();

            // create 3 discount for type percent
            Discount discount = new Discount();
            discount.setDiscountType(Discount.DiscountType.PERCENT);
            discount.setDiscountValue(20);
            // set start date 7 days ago
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); // Set the current date and time
            calendar.add(Calendar.DAY_OF_MONTH, -7); // Subtract 7 days
            discount.setStartDate(calendar.getTime());

            // set end date 7 days later
            calendar.add(Calendar.DAY_OF_MONTH, 14); // Subtract 7 days
            discount.setEndDate(calendar.getTime());
            discount.setProduct(product);
            discountRepository.save(discount);

        }

        for (int i = 4; i <= 6; i++) {
            // find 3 products
            Product product = productRepository.findById(i + 1L).get();

            // create 3 discount for type amount
            Discount discount = new Discount();
            discount.setDiscountType(Discount.DiscountType.AMOUNT);
            discount.setDiscountValue(10000);
            // set start date 7 days ago
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); // Set the current date and time
            calendar.add(Calendar.DAY_OF_MONTH, -7); // Subtract 7 days
            discount.setStartDate(calendar.getTime());

            // set end date 7 days later
            calendar.add(Calendar.DAY_OF_MONTH, 14); // Subtract 7 days
            discount.setEndDate(calendar.getTime());
            discount.setProduct(product);
            discountRepository.save(discount);
        }
    }
}
