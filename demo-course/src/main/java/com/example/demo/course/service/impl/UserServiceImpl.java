package com.example.demo.course.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.course.dao.UserDAO;
import com.example.demo.course.model.User;
import com.example.demo.course.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> getAllUser() {
        return userDAO.findAll();
    }
}
