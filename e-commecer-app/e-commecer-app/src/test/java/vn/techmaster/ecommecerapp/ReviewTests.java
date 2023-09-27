package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Product;
import vn.techmaster.ecommecerapp.entity.Review;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.repository.ProductRepository;
import vn.techmaster.ecommecerapp.repository.ReviewRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.util.List;
import java.util.Random;

@SpringBootTest
public class ReviewTests {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void save_reviews() {
        Random rd = new Random();
        Faker faker = new Faker();
        List<User> users = userRepository.findAll();
        Product product = productRepository.findById(1L).get();

        for (int i = 0; i < 50; i++) {
            User user = users.get(rd.nextInt(users.size()));
            Review review = new Review();
            review.setUser(user);
            review.setProduct(product);
            review.setRating(rd.nextInt(5) + 1);
            review.setComment(faker.lorem().paragraph());
            reviewRepository.save(review);
        }
    }

    @Test
    void save_reviews_other() {
        Random rd = new Random();
        Faker faker = new Faker();
        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            for (int j = 0; j < 5; j++) {
                User user = users.get(rd.nextInt(users.size()));
                Review review = new Review();
                review.setUser(user);
                review.setProduct(product);
                review.setRating(rd.nextInt(5) + 1);
                review.setComment(faker.lorem().paragraph());
                reviewRepository.save(review);
            }
        }
    }
}
