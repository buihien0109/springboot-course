package com.example.demo.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Integer id;
    private String name;
    private String email;
    private String country;
    private String gender;
    private LocalDate birthday;
    private String job;
    private List<String> hobbies;

}
