package com.example.todo.model;

import lombok.*;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private Integer id;
    private String title;
    private Boolean status;
}
