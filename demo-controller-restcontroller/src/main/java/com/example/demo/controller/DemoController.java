package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
* @Controller
*
* */
@Controller
public class DemoController {
    @GetMapping("/hello")
    public String hello() {
        return "hello"; // Returns the view name
    }

    @GetMapping("/api/blogs")
    @ResponseBody
    public String getBlogs() {
        return "Blog List"; // Returns the response body directly
    }
}
