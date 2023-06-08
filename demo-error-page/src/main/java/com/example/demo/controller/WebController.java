package com.example.demo.controller;

import com.example.demo.exception.ForbidenException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnauthorizeException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String getHomePage() {
        throw new RuntimeException("500");
    }

    @GetMapping("/about")
    public String getHomeAbout() {
        throw new NotFoundException("404");
    }

    @GetMapping("/contact")
    public String getContactPage() {
        throw new UnauthorizeException("401");
    }

    @GetMapping("/blog")
    public String getblogPage() {
        throw new ForbidenException("403");
    }
}
