package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// (exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class DemoJpaAuditingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJpaAuditingApplication.class, args);
    }

}
