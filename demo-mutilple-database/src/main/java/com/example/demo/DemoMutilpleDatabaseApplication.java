package com.example.demo;

import com.example.demo.db1.Post;
import com.example.demo.db1.PostRepository;
import com.example.demo.db2.Customer;
import com.example.demo.db2.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DemoMutilpleDatabaseApplication implements CommandLineRunner {
    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;

    public DemoMutilpleDatabaseApplication(PostRepository postRepository, CustomerRepository customerRepository) {
        this.postRepository = postRepository;
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoMutilpleDatabaseApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Lưu Post - MySQL
        Post post = new Post("Kết Nối Nhiều Cơ Sở Dữ Liệu trong JPA");
        postRepository.save(post);

        // Lưu Customer - Postgresql
        Customer customer = new Customer("Nguyễn Văn A");
        customerRepository.save(customer);

        // Thông tin Post
        List<Post> postList = postRepository.findAll();
        System.out.println("Danh sách Post");
        postList.forEach(System.out::println);

        // Thông tin customer
        List<Customer> customerList = customerRepository.findAll();
        System.out.println("Danh sách Customer");
        customerList.forEach(System.out::println);
    }
}
