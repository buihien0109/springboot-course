package com.example.demo.user.management.dao;

import com.example.demo.user.management.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> findAll();

    List<User> findByNameContainingIgnoreCase(String name);

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteById(Integer id);
}
