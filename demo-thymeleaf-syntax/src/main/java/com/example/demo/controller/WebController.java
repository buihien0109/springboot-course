package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {
    private List<User> userList;

    public WebController() {
        this.userList = new ArrayList<>();
        this.userList.add(new User(1, "Trần Tùng Dương", "duong@gmail.com"));
        this.userList.add(new User(2, "Trần Anh Quân", "quan@gmail.com"));
        this.userList.add(new User(3, "Bùi Thị Minh Nguyệt", "nguyet@gmail.com"));
        this.userList.add(new User(4, "Nguyễn Thị Thảo Nguyên", "nguyen@gmail.com"));
        this.userList.add(new User(5, "Tuấn Anh", "tuananh@gmail.com"));
    }

    @GetMapping("/")
    public String getHome(Model model) {
        // Trả về 1 giá trị
        model.addAttribute("user", userList.get(0));

        // Trả về 1 list
        model.addAttribute("users", userList);

        model.addAttribute("age", 16);
        model.addAttribute("day", 5);
        return "index";
    }
}
