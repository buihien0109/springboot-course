package com.example.demo.thymeleaf.crud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Controller này dùng để điều hướng các trang thymeleaf 123
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String viewHomePage() {
        return "dashboard";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "utils-page/unauthorized"; // Trang thymeleaf cho không có quyền truy cập
    }
}
