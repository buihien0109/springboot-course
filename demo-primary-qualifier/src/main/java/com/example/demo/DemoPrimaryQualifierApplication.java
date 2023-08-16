package com.example.demo;

import com.example.demo.model.IReadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoPrimaryQualifierApplication implements CommandLineRunner {
    @Qualifier("CSVReader")
    @Autowired
    private IReadFile fileReader;

    public static void main(String[] args) {
        SpringApplication.run(DemoPrimaryQualifierApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileReader.readFile("person");
    }
}
