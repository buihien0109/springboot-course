package com.example.demo;

import com.example.demo.model.User;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoJavaFakerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoJavaFakerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Tạo đối tượng với Java Faker
        User user = User.builder()
                .id((int) faker.number().randomNumber())
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .avatar(faker.avatar().image())
                .build();

        System.out.println("-- Thông tin user --");
        System.out.println(user);

        // Tạo danh sách đối tượng với Java Faker
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User userInfo = User.builder()
                    .id((int) faker.number().randomNumber())
                    .name(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .avatar(faker.avatar().image())
                    .build();
            userList.add(userInfo);
        }

        System.out.println("\n-- Danh sách user --");
        userList.forEach(System.out::println);
    }
}
