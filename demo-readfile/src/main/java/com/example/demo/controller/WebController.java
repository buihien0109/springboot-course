package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.WebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class WebController {
    private final WebService webService;

    @GetMapping("/api/users")
    public List<User> getAllUser() {
        return webService.getAllUser();
    }
}
