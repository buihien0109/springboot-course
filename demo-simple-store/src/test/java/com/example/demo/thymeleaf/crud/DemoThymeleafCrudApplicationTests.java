package com.example.demo.thymeleaf.crud;

import com.example.demo.thymeleaf.crud.entity.Product;
import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.repository.ProductRepository;
import com.example.demo.thymeleaf.crud.repository.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

@SpringBootTest
class DemoThymeleafCrudApplicationTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    void save_products() {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setName(faker.commerce().productName());
            product.setPrice(faker.number().numberBetween(10000, 1000000));
            product.setImageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8cHJvZHVjdHxlbnwwfHwwfHx8MA%3D%3D");
            productRepository.save(product);
        }
    }

    @Test
    void save_user() {
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(passwordEncoder.encode("123"));
            if (random.nextInt(3) == 0) {
                user.setRole("ROLE_ADMIN");
            } else if (random.nextInt(3) == 1) {
                user.setRole("ROLE_SALE");
            } else {
                user.setRole("ROLE_USER");
            }
            userRepository.save(user);
        }
    }
}
