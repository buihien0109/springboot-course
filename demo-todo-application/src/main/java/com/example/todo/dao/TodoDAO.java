package com.example.todo.dao;

import com.example.todo.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoDAO {
    List<Todo> findAll();

    List<Todo> findByStatus(Boolean status);

    Optional<Todo> findById(Integer id);

    Todo save(Todo todo);

    void deleteById(Integer id);
}
