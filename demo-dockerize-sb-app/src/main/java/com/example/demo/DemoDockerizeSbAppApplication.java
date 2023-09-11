package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class DemoDockerizeSbAppApplication implements CommandLineRunner {
    private final Environment environment;

    public DemoDockerizeSbAppApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoDockerizeSbAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("spring.mail.host = {}", environment.getProperty("spring.mail.host"));
        log.info("spring.mail.port = {}", environment.getProperty("spring.mail.port"));
        log.info("spring.mail.username = {}", environment.getProperty("spring.mail.username"));
        log.info("spring.mail.password = {}", environment.getProperty("spring.mail.password"));
        log.info("spring.datasource.url = {}", environment.getProperty("spring.datasource.url"));
        log.info("spring.datasource.username = {}", environment.getProperty("spring.datasource.username"));
        log.info("spring.datasource.password = {}", environment.getProperty("spring.datasource.password"));
    }
}
