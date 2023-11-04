package com.example.todo.db;

import com.example.todo.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoDB {
    public static List<Todo> todoList = new ArrayList<>(List.of(
            new Todo(1, "Đi đá bóng", false),
            new Todo(2, "Làm bài tập", true),
            new Todo(3, "Đi chơi với bạn bè", true)
    ));
}
