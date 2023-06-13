package com.example.demo.repo;

import com.example.demo.database.UserDB;
import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepo {
    public List<User> findAll() {
        return UserDB.userList;
    }
}
