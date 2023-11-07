package com.example.demo.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.course.db.TopicDB;
import com.example.demo.course.model.Course;
import com.example.demo.course.request.UpsertCourseRequest;
import com.example.demo.course.service.CourseService;
import com.example.demo.course.service.UserService;

import java.util.List;

@Controller
@RequestMapping("admin/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    @GetMapping
    public String getCourseListPage(Model model) {
        List<Course> courseList = courseService.getAllCourse();
        model.addAttribute("courseList", courseList);
        return "admin/course/index";
    }

    @GetMapping("/create")
    public String getCourseCreatePage(Model model) {
        model.addAttribute("topicList", TopicDB.topicList);
        model.addAttribute("userList", userService.getAllUser());
        return "admin/course/create";
    }

    @GetMapping("/{id}")
    public String getCourseDetailPage(@PathVariable Integer id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("topicList", TopicDB.topicList);
        model.addAttribute("userList", userService.getAllUser());

        return "admin/course/detail";
    }
}
