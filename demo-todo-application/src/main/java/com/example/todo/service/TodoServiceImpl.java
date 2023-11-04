package com.example.todo.service;

import com.example.todo.dao.TodoDAO;
import com.example.todo.exception.ResourceNotFoundException;
import com.example.todo.model.Todo;
import com.example.todo.request.CreateTodoRequest;
import com.example.todo.request.UpdateTodoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoDAO todoDAO;

    @Override
    public List<Todo> getAllTodos() {
        return todoDAO.findAll();
    }

    @Override
    public List<Todo> getAllTodos(Optional<Boolean> status) {
        if (status.isPresent()) {
            return todoDAO.findByStatus(status.get());
        }
        return todoDAO.findAll();
    }

    @Override
    public Todo getTodoById(Integer id) {
        return todoDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id = " + id));
    }

    @Override
    public Todo createTodo(CreateTodoRequest request) {
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .status(false)
                .build();
        return todoDAO.save(todo);
    }

    @Override
    public Todo updateTodo(Integer id, UpdateTodoRequest request) {
        Todo todo = todoDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id = " + id));

        todo.setTitle(request.getTitle());
        todo.setStatus(request.getStatus());

        return todoDAO.save(todo);
    }

    @Override
    public void deleteTodo(Integer id) {
        Todo todo = todoDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id = " + id));
        todoDAO.deleteById(id);
    }
}
