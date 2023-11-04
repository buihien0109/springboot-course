package com.example.todo.rest;

import com.example.todo.request.CreateTodoRequest;
import com.example.todo.request.UpdateTodoRequest;
import com.example.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/todos")
@RequiredArgsConstructor
public class TodoResources {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<?> getAllTodo(@RequestParam Optional<Boolean> status) {
        return ResponseEntity.ok(todoService.getAllTodos(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable int id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody CreateTodoRequest request) {
        return ResponseEntity.ok(todoService.createTodo(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable int id, @RequestBody UpdateTodoRequest request) {
        return ResponseEntity.ok(todoService.updateTodo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable int id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}