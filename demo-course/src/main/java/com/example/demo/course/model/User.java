package com.example.demo.course.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String avatar;
}
