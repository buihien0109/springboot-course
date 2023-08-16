package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class JsonReader implements IReadFile {

    @Override
    public void readFile(String fileName) {
        System.out.println("Read file json : " + fileName);
    }
}
