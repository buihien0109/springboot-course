package com.example.demo;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoBeanApplication implements CommandLineRunner {

    @Autowired
    private User user;

    @Autowired
    private Post post;

    public static void main(String[] args) {
        SpringApplication.run(DemoBeanApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(user);

        post.setId(1);
        post.setTitle("New Post");
        System.out.println(post);
    }
}
