package com.example.demomockmvctest.controller;

import com.example.demomockmvctest.model.Post;
import com.example.demomockmvctest.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 1. Lấy ds tất cả post
    @GetMapping
    public ResponseEntity<?> getAllPost() {
        return ResponseEntity.ok(postService.getAllPost()); // status = 200, body = List<Post>
    }

    // 2. Lấy post theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // 3. Tạo mới
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody Post request) {
        return new ResponseEntity<>(postService.createPost(request), HttpStatus.CREATED); // status = 201
    }

    // 4. Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Integer id, @Valid @RequestBody Post request) {
        return ResponseEntity.ok().body(postService.updatePost(id, request));
    }

    // 5. Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build(); // status = 204
    }

    // 6. Tìm kiếm
    @GetMapping("/search")
    public ResponseEntity<?> searchPost(@RequestParam String title) {
        return ResponseEntity.ok(postService.searchPost(title));
    }
}
