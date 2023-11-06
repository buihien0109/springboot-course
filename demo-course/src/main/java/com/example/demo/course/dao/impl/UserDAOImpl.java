package com.example.demo.course.dao.impl;

import org.springframework.stereotype.Repository;
import com.example.demo.course.dao.UserDAO;
import com.example.demo.course.db.UserDB;
import com.example.demo.course.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {
    @Override
    public List<User> findAll() {
        return UserDB.userList;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return UserDB.userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
}
