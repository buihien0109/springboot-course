package com.example.author.persistence.repository;


import com.example.author.persistence.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}