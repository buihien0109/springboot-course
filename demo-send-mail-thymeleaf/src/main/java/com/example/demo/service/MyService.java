package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MyService {
    private final EmailService emailService;

    public MyService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendComplexEmail() {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "Nguyễn Minh Duy");
        model.put("content", "<p>This is a <strong>complex</strong> email with HTML content and CSS styling.</p>");

        emailService.sendEmail("duy@gmail.com", "Important Notification", model, "email-template");
    }
}
