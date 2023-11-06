package com.example.demo.course.rest;

import com.example.demo.course.db.TopicDB;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/v1/topics")
public class TopipResources {
    @GetMapping
    public ResponseEntity<?> getAllTopic() {
        return ResponseEntity.ok(TopicDB.topicList);
    }
}
