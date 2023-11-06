package com.example.demo.course.dao;

import com.example.demo.course.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    List<Course> findAll();

    Optional<Course> findById(Integer id);

    void deleteById(Integer id);

    Course save(Course course);
}
