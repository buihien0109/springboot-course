package com.example.demo.controller;

import com.example.demo.model.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/books")
public class BookController {
    private final ConcurrentHashMap<String, Book> books;
    public BookController() {
        books = new ConcurrentHashMap<>();
        books.put("OX-13", new Book("OX-13", "Gone with the wind", "Cuong", 1945));
        books.put("OX-14", new Book("OX-14", "Chi Dau", "Nam Cao", 1943));
    }

    @GetMapping
    public List<Book> getBooks() {
        return books.values().stream().toList();
    }
}
