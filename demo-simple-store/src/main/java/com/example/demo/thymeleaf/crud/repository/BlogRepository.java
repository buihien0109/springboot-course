package com.example.demo.thymeleaf.crud.repository;

import com.example.demo.thymeleaf.crud.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByTitleContaining(String name, Pageable pageable);

    List<Blog> findByIsPublished(Boolean isPublished);
}