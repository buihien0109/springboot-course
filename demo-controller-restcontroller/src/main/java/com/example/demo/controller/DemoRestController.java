package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoRestController {
    @GetMapping("/api/users")
    public String getUsers() {
        return "User List"; // Returns the response body directly
    }
}
