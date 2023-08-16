package com.example.demo.controller;

import com.example.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class WebController {
    private List<User> userList;

    public WebController() {
        this.userList = new ArrayList<>();
        this.userList.add(new User(1, "Trần Tùng Dương", "duong@gmail.com"));
        this.userList.add(new User(2, "Trần Anh Quân", "quan@gmail.com"));
        this.userList.add(new User(3, "Bùi Minh Nguyệt", "nguyet@gmail.com"));
        this.userList.add(new User(4, "Nguyễn Thảo Nguyên", "nguyen@gmail.com"));
        this.userList.add(new User(5, "Tuấn Anh", "tuananh@gmail.com"));
    }

    @GetMapping("/users")
    public String getUserPage(@RequestParam(required = false) String name, Model model) {
        List<User> users = userList.stream()
                .filter(user -> name == null || user.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        return "user";
    }
}
