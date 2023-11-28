package com.example.author.persistence.repository;


import com.example.author.persistence.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}