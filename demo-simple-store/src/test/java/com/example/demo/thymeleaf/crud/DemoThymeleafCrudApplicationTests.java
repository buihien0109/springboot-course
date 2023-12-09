package com.example.demo.thymeleaf.crud;

import com.example.demo.thymeleaf.crud.entity.Blog;
import com.example.demo.thymeleaf.crud.entity.Product;
import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.repository.BlogRepository;
import com.example.demo.thymeleaf.crud.repository.ProductRepository;
import com.example.demo.thymeleaf.crud.repository.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.management.relation.Role;
import java.util.List;
import java.util.Random;

@SpringBootTest
class DemoThymeleafCrudApplicationTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private BlogRepository blogRepository;


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
    void update_description_product() {
        Faker faker = new Faker();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            product.setDescription(faker.lorem().sentence(50));
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

    @Test
    void save_blog() {
        Faker faker = new Faker();
        Random random = new Random();

        List<User> users = userRepository.findByRoleIn(List.of("ROLE_ADMIN", "ROLE_SALE"));

        for (int i = 0; i < 20; i++) {
            Blog blog = new Blog();
            blog.setTitle(faker.book().title());
            blog.setContent(faker.lorem().sentence(300));
            blog.setDescription(faker.lorem().sentence(10));
            blog.setImageUrl("https://images.unsplash.com/photo-1682686581362-796145f0e123?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHw2fHx8ZW58MHx8fHx8");
            blog.setAuthor(users.get(random.nextInt(users.size())));
            blog.setIsPublished(random.nextBoolean());
            blogRepository.save(blog);
        }
    }
}
