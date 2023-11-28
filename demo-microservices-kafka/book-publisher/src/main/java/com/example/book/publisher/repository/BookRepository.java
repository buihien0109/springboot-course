package com.example.book.publisher.repository;

import com.example.book.publisher.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}