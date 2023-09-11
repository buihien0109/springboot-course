package com.example.demo;

import com.example.demo.request.LoginRequest;
import com.example.demo.request.RegisterRequest;
import com.example.demo.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class DemoChatAppBackendApplicationTests {

    @Autowired
    private ChatService chatService;

    @Test
    void test_login() throws JsonProcessingException {
        LoginRequest request = new LoginRequest("hien", "123");
        Map<String, Object> rs = chatService.login(request);
        System.out.println(rs);
    }

    @Test
    void test_register() throws JsonProcessingException {
        RegisterRequest request = new RegisterRequest("hang", "123", "hang@gmail.com", "thu", "hang");
        Map<String, Object> rs = chatService.register(request);
        System.out.println(rs);
    }

}
