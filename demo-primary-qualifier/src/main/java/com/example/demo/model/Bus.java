package com.example.demo.model;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class Bus implements Vehicle {

    @Override
    public void run() {
        System.out.println("Run by Bus");
    }
}
