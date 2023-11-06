package com.example.demo.course.controller;

import com.example.demo.course.db.TopicDB;
import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.service.WebService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/khoa-hoc")
public class WebController {
    private final WebService webService;

    public WebController(WebService webService) {
        this.webService = webService;
    }

    @GetMapping
    public String getCoursePage(Model model) {
        List<CourseDto> courseDtoList = webService.getAllCourse(null, null, null);
        model.addAttribute("courseList", courseDtoList);
        model.addAttribute("topicList", TopicDB.topicList);

        return "web/course-list";
    }

    @GetMapping("/online")
    public String getCourseOnlinePage(Model model) {
        List<CourseDto> courseDtoList = webService.getAllCourse("online", null, null);
        model.addAttribute("courseList", courseDtoList);
        model.addAttribute("topicList", TopicDB.topicList);

        return "web/course-online-list";
    }

    @GetMapping("/onlab")
    public String getCourseOnlabPage(Model model) {
        List<CourseDto> courseDtoList = webService.getAllCourse("onlab", null, null);
        model.addAttribute("courseList", courseDtoList);
        model.addAttribute("topicList", TopicDB.topicList);

        return "web/course-onlab-list";
    }

    @GetMapping("/{id}")
    public String getCourseDetailPage(Model model, @PathVariable Integer id) {
        CourseDto courseDto = webService.getCourseById(id);
        model.addAttribute("course", courseDto);

        return "web/detail";
    }
}
