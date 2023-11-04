package com.example.todo.service;


import com.example.todo.model.Todo;
import com.example.todo.request.CreateTodoRequest;
import com.example.todo.request.UpdateTodoRequest;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<Todo> getAllTodos();

    List<Todo> getAllTodos(Optional<Boolean> status);

    Todo getTodoById(Integer id);

    Todo createTodo(CreateTodoRequest request);

    Todo updateTodo(Integer id, UpdateTodoRequest request);

    void deleteTodo(Integer id);
}
