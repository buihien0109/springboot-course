package com.example.demo.course.rest;

import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.service.WebService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/courses")
@RequiredArgsConstructor
public class WebResources {
    private final WebService webService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourse(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String topic) {
        List<CourseDto> courseDtoList = webService.getAllCourse(type, name, topic);
        return ResponseEntity.ok(courseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Integer id) {
        CourseDto courseDto = webService.getCourseById(id);
        return ResponseEntity.ok(courseDto);
    }
}
