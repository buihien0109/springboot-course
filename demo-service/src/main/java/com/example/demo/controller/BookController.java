package com.example.demo.controller;

import com.example.demo.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/")
    public String getBooks() {
        return bookService.getAllBooks();
    }
}
