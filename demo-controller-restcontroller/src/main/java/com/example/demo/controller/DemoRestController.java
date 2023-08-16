package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoRestController {
    @GetMapping("/api/users")
    public List<String> getUsers() {
        List<String> userList = List.of("User 1", "User 2", "User 3");
        return userList; // Trả về JSON, XML, ... trong body response
    }
}
