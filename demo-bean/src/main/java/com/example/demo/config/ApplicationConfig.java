package com.example.demo.config;

import com.example.demo.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public User user() {
        return new User(1, "Maria");
    }
}
