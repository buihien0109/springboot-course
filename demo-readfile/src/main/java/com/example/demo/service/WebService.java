package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.utils.ReadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebService {
    private List<User> userList;
    private final ReadFile fileReader;

    @Autowired
    public WebService(@Qualifier("ExcelReadFile") ReadFile fileReader) {
        this.fileReader = fileReader;
        this.userList = this.fileReader.readFile("classpath:static/users.xlsx");
    }

    public List<User> getAllUser() {
        return userList;
    }
}
