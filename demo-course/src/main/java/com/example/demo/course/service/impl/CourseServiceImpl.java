package com.example.demo.course.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.course.dao.CourseDAO;
import com.example.demo.course.exception.ResouceNotFoundException;
import com.example.demo.course.model.Course;
import com.example.demo.course.request.UpsertCourseRequest;
import com.example.demo.course.service.CourseService;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO;

    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Override
    public List<Course> getAllCourse() {
        return courseDAO.findAll();
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseDAO.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Not found course"));
    }

    @Override
    public Course createCourse(UpsertCourseRequest request) {
        Course course = Course.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .topics(request.getTopics())
                .thumbnail(request.getThumbnail())
                .userId(request.getUserId())
                .build();

        return courseDAO.save(course);
    }

    @Override
    public Course updateCourse(Integer id, UpsertCourseRequest request) {
        Course course = courseDAO.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Not found course"));

        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setType(request.getType());
        course.setTopics(request.getTopics());
        course.setThumbnail(request.getThumbnail());
        course.setUserId(request.getUserId());

        return course;
    }

    @Override
    public void deleteCourse(Integer id) {
        Course course = courseDAO.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Not found course"));

        courseDAO.deleteById(id);
    }
}
