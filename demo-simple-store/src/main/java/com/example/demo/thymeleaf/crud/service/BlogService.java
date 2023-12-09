package com.example.demo.thymeleaf.crud.service;

import com.example.demo.thymeleaf.crud.entity.Blog;
import com.example.demo.thymeleaf.crud.entity.User;
import com.example.demo.thymeleaf.crud.repository.BlogRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final HttpSession session;

    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    public List<Blog> findAllBlogsPublished() {
        return blogRepository.findByIsPublished(true)
                .stream()
                .sorted((b1, b2) -> b2.getPublishedAt().compareTo(b1.getPublishedAt()))
                .toList();
    }

    public Page<Blog> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return blogRepository.findAll(pageable);
    }

    public Page<Blog> findPaginatedWithSearch(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        if (keyword != null && !keyword.isEmpty()) {
            return blogRepository.findByTitleContaining(keyword, pageable);
        }
        return blogRepository.findAll(pageable);
    }

    public void save(Blog blog) {
        log.info("Saving blog: " + blog);

        User user = (User) session.getAttribute("currentUser");
        log.info("user: " + user);

        blog.setAuthor(user);
        blogRepository.save(blog);
    }

    public Optional<Blog> findById(Long id) {
        return blogRepository.findById(id);
    }

    public void delete(Long id) {
        blogRepository.deleteById(id);
    }
}
