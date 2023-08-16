package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoLombokApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoLombokApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Tạo user sử dụng constructor
        User user = new User(1,  "Nguyễn Văn A", "a@gmail.com");

        // Tạo user sử dụng builder
        User user1 = User.builder()
                .id(2)
                .name("Trần Văn B")
                .email("b@gmail.com")
                .build();

        // In ra thông tin của user
        System.out.println("== THÔNG TIN USER");
        System.out.println(user);
        System.out.println(user1);

        // Lấy ra các thuộc tính của user
        System.out.println("\n== THUỘC TÍNH USER");
        System.out.println("Id : " + user.getId());
        System.out.println("Name : " + user.getName());
        System.out.println("Email : " + user.getEmail());

        // Set lại các thuộc tính của user
        user.setName("Ngô Thị C");
        user.setEmail("c@gmail.com");
        System.out.println("\n== THÔNG TIN USER SAU KHI CẬP NHẬT");
        System.out.println(user);

    }
}
