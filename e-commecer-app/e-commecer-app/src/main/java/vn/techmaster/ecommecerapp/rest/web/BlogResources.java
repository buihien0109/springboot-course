package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.model.request.UpsertBlogRequest;
import vn.techmaster.ecommecerapp.service.BlogService;

@RestController
@RequestMapping("/api/v1/admin/blogs")
@RequiredArgsConstructor
public class BlogResources {
    private final BlogService blogService;

    // 1. Tạo bài viết
    @PostMapping
    public ResponseEntity<?> createBlog(@RequestBody UpsertBlogRequest request) {
        return new ResponseEntity<>(blogService.createBlog(request), HttpStatus.CREATED); // 201
    }

    // 2. Cập nhật bài viết
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlogById(@PathVariable Integer id, @RequestBody UpsertBlogRequest request) {
        return ResponseEntity.ok(blogService.updateBlogById(id, request)); // 200
    }

    // 3. Xóa bài viết
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogById(@PathVariable Integer id) {
        blogService.deleteBlogById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
