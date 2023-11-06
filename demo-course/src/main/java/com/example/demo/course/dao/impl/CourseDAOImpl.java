package com.example.demo.course.dao.impl;

import org.springframework.stereotype.Repository;
import com.example.demo.course.dao.CourseDAO;
import com.example.demo.course.db.CourseDB;
import com.example.demo.course.model.Course;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseDAOImpl implements CourseDAO {
    @Override
    public List<Course> findAll() {
        return CourseDB.courseList;
    }

    @Override
    public Optional<Course> findById(Integer id) {
        return CourseDB.courseList.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteById(Integer id) {
        CourseDB.courseList.removeIf(course -> course.getId().equals(id));
    }

    @Override
    public Course save(Course course) {
        course.setId(createId());
        CourseDB.courseList.add(course);
        return course;
    }

    private Integer createId() {
        List<Course> courseList = CourseDB.courseList;
        return courseList.stream()
                .map(Course::getId)
                .mapToInt(Integer::valueOf)
                .max().orElse(0) + 1;
    }
}
