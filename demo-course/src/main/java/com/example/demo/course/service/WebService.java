package com.example.demo.course.service;

import com.example.demo.course.dto.CourseDto;

import java.util.List;

public interface WebService {
    List<CourseDto> getAllCourse(String type, String name, String topic);

    CourseDto getCourseById(Integer id);
}
