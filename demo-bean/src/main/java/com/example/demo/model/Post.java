package com.example.demo.model;

import lombok.*;
import org.springframework.stereotype.Component;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Post {
    private Integer id;
    private String title;
}
