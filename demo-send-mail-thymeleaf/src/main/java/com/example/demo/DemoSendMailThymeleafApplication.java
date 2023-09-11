package com.example.demo;

import com.example.demo.service.MyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoSendMailThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSendMailThymeleafApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(MyService myService) {
        return args -> {
            myService.sendComplexEmail();
        };
    }
}
