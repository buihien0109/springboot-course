package org.example.demo.db;

import org.example.demo.utils.file_utils.IFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitDB implements CommandLineRunner {
    @Autowired
    private IFileReader fileReader;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("DB is initialized");

        ProductDB.products = fileReader.readFile("product.json");
        System.out.println("Book size : " + ProductDB.products);

        ProductDB.products.forEach(System.out::println);
    }
}
