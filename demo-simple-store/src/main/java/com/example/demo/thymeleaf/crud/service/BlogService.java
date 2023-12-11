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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final HttpSession session;
    private final FileService fileService;

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

    public void save(Blog blog, MultipartFile file) throws IOException {
        // Lưu ảnh vào folder
        String filePath = fileService.saveFile(file);
        blog.setImageUrl(filePath);

        // Lưu blog vào database
        User user = (User) session.getAttribute("currentUser");
        blog.setAuthor(user);
        blogRepository.save(blog);
    }

    public void update(Blog blog, MultipartFile file) throws IOException {
        log.info("Blog: {}", blog);

        Blog existingBlog = blogRepository.findById(blog.getId()).orElse(null);
        if (existingBlog != null) {
            log.info("Existing Blog: {}", existingBlog);

            existingBlog.setTitle(blog.getTitle());
            existingBlog.setContent(blog.getContent());
            existingBlog.setDescription(blog.getDescription());
            existingBlog.setIsPublished(blog.getIsPublished());

            // Kiểm tra xem có file ảnh mới không
            if (file != null && !file.isEmpty()) {
                log.info("New Image");
                String filePath = fileService.saveFile(file);
                existingBlog.setImageUrl(filePath);
            }

            // Lưu blog vào database
            blogRepository.save(existingBlog);
        }
    }

    public Optional<Blog> findById(Long id) {
        return blogRepository.findById(id);
    }

    public void delete(Long id) {
        blogRepository.deleteById(id);
    }
}
