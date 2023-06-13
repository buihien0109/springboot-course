package com.example.demo.controller;

import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/contact")
    public String getContactPage() {
        throw new NotFoundException("404");
    }

    @GetMapping("/about")
    public String getAboutPage() {
        throw new RuntimeException("500");
    }
}
