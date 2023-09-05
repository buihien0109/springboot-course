package com.example.demo.repository;

import com.example.demo.database.TaskDB;
import com.example.demo.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {
    public List<Task> findAll() {
        return TaskDB.taskList;
    }

    public Optional<Task> findById(Long id) {
        return TaskDB.taskList.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    public Task save(Task task) {
        Long id = TaskDB.taskList.stream()
                .map(Task::getId)
                .max(Long::compare).orElse(1L);

        task.setId(id);
        TaskDB.taskList.add(task);
        return task;
    }

    public void deleteById(Long id) {
        TaskDB.taskList.removeIf(task -> task.getId().equals(id));
    }
}
