package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {
    public List<User> userList;

    @GetMapping("/")
    public String getHome() {
        // Trả về 1 giá trị

        // Trả về 1 list
        return "index";
    }
}
