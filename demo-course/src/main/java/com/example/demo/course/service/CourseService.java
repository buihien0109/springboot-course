package com.example.demo.course.service;

import com.example.demo.course.model.Course;
import com.example.demo.course.request.UpsertCourseRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourse();

    Course getCourseById(Integer id);

    Course createCourse(UpsertCourseRequest request);

    Course updateCourse(Integer id, UpsertCourseRequest request);

    void deleteCourse(Integer id);

    String uploadThumbnail(Integer id, MultipartFile file);
}
