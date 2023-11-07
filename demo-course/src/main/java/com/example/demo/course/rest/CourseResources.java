package com.example.demo.course.rest;

import com.example.demo.course.model.Course;
import com.example.demo.course.request.UpsertCourseRequest;
import com.example.demo.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/courses")
@RequiredArgsConstructor
public class CourseResources {
    private final CourseService courseService;

    // 1. Lấy danh sách tất cả khóa học
    @GetMapping
    public ResponseEntity<?> getAllCourse() {
        List<Course> courseList = courseService.getAllCourse();
        return ResponseEntity.ok(courseList);
    }

    // 2. Lấy chi tiết khóa học
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Integer id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    // 3. Tạo khóa học
    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody UpsertCourseRequest request) {
        Course course = courseService.createCourse(request);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    // 4. Cập nhật thông tin khóa học
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Integer id, @Valid @RequestBody UpsertCourseRequest request) {
        Course course = courseService.updateCourse(id, request);
        return ResponseEntity.ok(course);
    }

    // 5. Xóa khóa học
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // 6. Cập nhật ảnh khóa học
    @PostMapping("/{id}/upload-thumbnail")
    public ResponseEntity<?> uploadThumbnail(@RequestParam MultipartFile file, @PathVariable Integer id) {
        String thumbnailPath = courseService.uploadThumbnail(id, file);
        return ResponseEntity.ok(thumbnailPath);
    }
}
