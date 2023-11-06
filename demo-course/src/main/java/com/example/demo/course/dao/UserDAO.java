package com.example.demo.course.dao;

import com.example.demo.course.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> findAll();

    Optional<User> findById(Integer id);
}
