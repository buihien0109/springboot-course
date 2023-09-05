package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task API", description = "Endpoints for managing tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID", description = "Retrieve detailed information about a specific task")
    @ApiResponse(responseCode = "200", description = "Task found successfully", content = @Content(schema = @Schema(implementation = Task.class)))
    @ApiResponse(responseCode = "404", description = "Task not found")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "ID of the task to retrieve", required = true)
            @PathVariable Long id) {

        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a task", description = "Create a new task")
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task by ID", description = "Update an existing task by ID")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Long id,
            @RequestBody Task task) {

        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(task.getTitle());
                    existingTask.setDescription(task.getDescription());
                    return ResponseEntity.ok(taskRepository.save(existingTask));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by ID", description = "Delete a task by ID")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Long id) {

        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
