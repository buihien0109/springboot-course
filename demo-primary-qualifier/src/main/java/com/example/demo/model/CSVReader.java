package com.example.demo.model;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class CSVReader implements IReadFile {

    @Override
    public void readFile(String fileName) {
        System.out.println("Read file CSV : " + fileName);
    }
}
