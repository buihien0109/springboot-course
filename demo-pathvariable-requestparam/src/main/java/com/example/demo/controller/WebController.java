package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    // == Sử dụng @PathVariable ==
    // http://localhost:8080/api/users/1
    // http://localhost:8080/api/users/2
    // http://localhost:8080/api/users/3
    @GetMapping("api/users/{id}")
    public String getUser(@PathVariable Integer id) {
        return "Id : " + id;
    }

    // http://localhost:8080/api/tags/backend
    // http://localhost:8080/api/tags/frontend
    @GetMapping("api/tags/{tagName}")
    public String getTag(@PathVariable(name = "tagName") String name) {
        return "Name : " + name;
    }

    // == Sử dụng @RequestParam ==
    // http://localhost:8080/api/users?name=an
    // http://localhost:8080/api/users --> Lỗi vì name bắt buộc phải có trên URL
    @GetMapping("api/users")
    public String getAllUser(@RequestParam String name) {
        return "Name : " + name;
    }

    // Sử dụng @RequestParam
    // http://localhost:8080/api/products -> Lấy tất cả product (minPrice không bắt buộc phải có trên URL)
    // http://localhost:8080/api/products?minPrice=1000 -> Lấy tất cả product có giá >= 1000
    @GetMapping("api/products")
    public String getAllProduct(@RequestParam(required = false) Integer minPrice) {
        return "minPrice : " + minPrice;
    }
}
