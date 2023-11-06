package com.example.demo.course.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.course.dao.CourseDAO;
import com.example.demo.course.dao.UserDAO;
import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.exception.ResouceNotFoundException;
import com.example.demo.course.model.Course;
import com.example.demo.course.model.User;
import com.example.demo.course.service.WebService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebServiceImpl implements WebService {

    private final CourseDAO courseDAO;
    private final UserDAO userDAO;

    public WebServiceImpl(CourseDAO courseDAO, UserDAO userDAO) {
        this.courseDAO = courseDAO;
        this.userDAO = userDAO;
    }

    @Override
    public List<CourseDto> getAllCourse(String type, String name, String topic) {
        List<Course> courseList = courseDAO.findAll();
        return courseList.stream()
                .filter(course -> (type == null || course.getType().equals(type))
                        && (name == null || course.getName().toLowerCase().contains(name.toLowerCase()))
                        && (topic == null || course.getTopics().contains(topic)))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(Integer id) {
        Course course = courseDAO.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Not found course"));

        return mapToDto(course);
    }

    private CourseDto mapToDto(Course course) {
        User user = userDAO.findById(course.getUserId())
                .orElseThrow(() -> new ResouceNotFoundException("Not found user"));
        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .type(course.getType())
                .topics(course.getTopics())
                .thumbnail(course.getThumbnail())
                .user(user)
                .build();
    }
}
