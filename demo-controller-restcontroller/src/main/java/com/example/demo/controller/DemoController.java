package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DemoController {
    @GetMapping("/hello")
    public String hello() {
        return "hello"; // Trả về view (template)
    }

    @GetMapping("/api/blogs")
    @ResponseBody
    public List<String> getBlogs() {
        List<String> blogs = List.of("Blog 1", "Blog 2", "Blog 3");
        return blogs; // Trả về JSON, XML, ... trong body response
    }
}
