package com.example.demo.stream.people.utils;

import com.example.demo.stream.people.model.Person;

import java.util.List;

public interface ReadFile {
    List<Person> readFile(String filePath);
}
