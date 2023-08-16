package com.example.demo.model;

import lombok.*;
import org.springframework.stereotype.Component;

@ToString
@Getter
@Setter
@AllArgsConstructor
@Component
public class Post {
    private Integer id;
    private String title;

    public Post() {
        System.out.println("Post created");
    }
}
