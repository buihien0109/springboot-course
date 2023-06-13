package com.example.demo.service;

import com.example.demo.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BookService {
    private List<Book> books;

    public BookService() {
        this.books = new ArrayList<>();
        this.books.add(new Book(1, " Nhà Giả Kim", 1990));
        this.books.add(new Book(2, " Đắc Nhân Tâm", 2006));
        this.books.add(new Book(3, " Cách nghĩ để thành công", 2000));
        this.books.add(new Book(4, " Hạt giống tâm hồn", 1995));
        this.books.add(new Book(5, " Đọc Vị Bất Kỳ Ai", 2013));
    }

    // Lấy tất cả sách
    public List<Book> getAllBook() {
        return this.books;
    }

    // Lấy sách theo id
    public Book getBookById(Integer id) {
        return this.books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    throw new RuntimeException("Not found book with id = " + id);
                });
    }

    // Sắp xếp sách tăng dần theo ngày ra mắt
    public List<Book> sortBooksByReleaseYear() {
        return this.books.stream()
                .sorted(Comparator.comparing(Book::getReleaseYear))
                .toList();
    }
}
