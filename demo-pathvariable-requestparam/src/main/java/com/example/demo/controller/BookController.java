package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    @GetMapping("/book/{id}")
    public String getBookByPath(@PathVariable("id") int id) {
        return "Book id = " + id;
    }

    @GetMapping("/bookquery")
    public String getBookByQuery(@RequestParam(value = "id", required = true) int id) {
        return "Book id = " + id;
    }
}
