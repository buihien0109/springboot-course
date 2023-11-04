package com.example.todo.dao;

import com.example.todo.db.TodoDB;
import com.example.todo.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TodoDAOImpl implements TodoDAO {
    @Override
    public List<Todo> findAll() {
        return TodoDB.todoList;
    }

    @Override
    public List<Todo> findByStatus(Boolean status) {
        return TodoDB.todoList.stream()
                .filter(todo -> todo.getStatus().equals(status))
                .toList();
    }

    @Override
    public Optional<Todo> findById(Integer id) {
        return TodoDB.todoList.stream()
                .filter(todo -> todo.getId().equals(id)).findFirst();
    }

    @Override
    public Todo save(Todo todo) {
        Optional<Todo> todoOptional = findById(todo.getId());
        if (todoOptional.isPresent()) {
            Todo todoUpdate = todoOptional.get();
            todoUpdate.setTitle(todo.getTitle());
            todoUpdate.setStatus(todo.getStatus());
            return todoUpdate;
        } else {
            todo.setId(createId());
            TodoDB.todoList.add(todo);
            return todo;
        }
    }

    @Override
    public void deleteById(Integer id) {
        TodoDB.todoList.removeIf(todo -> todo.getId().equals(id));
    }

    private Integer createId() {
        List<Todo> todoList = TodoDB.todoList;
        return todoList.stream()
                .map(Todo::getId)
                .mapToInt(Integer::valueOf)
                .max().orElse(0) + 1;
    }
}
