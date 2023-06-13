package com.example.demo.database;

import com.example.demo.model.User;

import java.util.List;

public class UserDB {
    public static List<User> userList = List.of(
            new User(1, "Nguyễn Văn A", "a@gmail.com"),
            new User(2, "Trần Văn B", "b@gmail.com"),
            new User(3, "Ngô Thị C", "c@gmail.com"),
            new User(4, "Bùi Văn D", "d@gmail.com")
    );
}
