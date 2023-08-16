package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    public UserController() {
        System.out.println("UserController created");
    }

    @GetMapping("/")
    public String getHome() {
        return "Home page";
    }
}
