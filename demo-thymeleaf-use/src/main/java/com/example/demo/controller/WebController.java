package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String getHome() {
        return "index";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "html/about";
    }
}
