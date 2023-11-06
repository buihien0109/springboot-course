package com.example.demo.course.db;

import com.example.demo.course.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDB {
    public static List<Course> courseList = new ArrayList<>(
            List.of(
                    new Course(1, "Data Structure - Algorithm - LeetCode", "Description 1", "online", List.of("backend"), "https://techmaster.vn/resources/image/thumbnail.jpg", 1),
                    new Course(2, "Luyện thi FE", "Description 2", "onlab", List.of("basic"), "https://media.techmaster.vn/api/static/36/uhWHezPC", 1),
                    new Course(3, "Java cấu trúc dữ liệu - giải thuật", "Description 3", "onlab", List.of("basic", "backend"), "https://media.techmaster.vn/api/static/36/bu7v5ak51co41h2qctt0", 1),
                    new Course(4, "Java căn bản", "Description 4", "online", List.of("frontend", "backend", "devops"), "https://media.techmaster.vn/api/static/bub3enc51co7s932dsk0/ZuedW7J1", 1),
                    new Course(5, "Spring Boot - Web Back End", "Description 5", "onlab", List.of("devops", "basic", "mobile"), "https://media.techmaster.vn/api/static/36/bu7v9ks51co41h2qcttg", 2),
                    new Course(6, "Học lập trình Java trực tuyến", "Description 6", "onlab", List.of("database", "devops"), "https://media.techmaster.vn/api/fileman/Uploads/Java/banner-javabasic.png", 2),
                    new Course(7, "Lập trình Web - API - Microservice bằng Golang", "Description 7", "onlab", List.of("backend", "database", "devops"), "https://media.techmaster.vn/api/static/36/bu803kc51co41h2qctug", 2),
                    new Course(8, "Lập trình Lego Robot Inventor", "Description 8", "online", List.of("basic", "frontend"), "https://media.techmaster.vn/api/static/brbgh4451coepbqoch60/eQcu6FM-", 2),
                    new Course(9, "Lộ trình DevOps", "Description 9", "online", List.of("devops"), "https://devops.techmaster.vn/resources/image/banner_devops.png", 3),
                    new Course(10, "Nhập môn lập trình - giải thuật cơ bản", "Description 10", "onlab", List.of("backend"), "https://media.techmaster.vn/api/fileman/Uploads/users/5463/giaithuat-thumnail.png", 3),
                    new Course(11, "Lập trình di động IOS Swift online", "Description 11", "onlab", List.of("mobile"), "https://media.techmaster.vn/api/static/8028/bqa348s51cocm3n1ubq0", 3),
                    new Course(12, "Lập trình Nodejs (cập nhật 2022)", "Description 12", "online", List.of("basic", "backend"), "https://media.techmaster.vn/api/static/bub3enc51co7s932dsk0/6PyUoB-T", 3)
            )
    );
}
