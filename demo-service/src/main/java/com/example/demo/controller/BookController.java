package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    // API : Lấy tất cả sách
    @GetMapping("/api/books")
    public List<Book> getBooks() {
        return bookService.getAllBook();
    }

    // API : Lấy chi tiết sách theo id
    @GetMapping("/api/books/{id}")
    public Book getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

    // API : Sắp xếp sách tăng dần theo ngày ra mắt
    @GetMapping("/api/books/sort")
    public List<Book> sortBooksByReleaseYear() {
        return bookService.sortBooksByReleaseYear();
    }
}
