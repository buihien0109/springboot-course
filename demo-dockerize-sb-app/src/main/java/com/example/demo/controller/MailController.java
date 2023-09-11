package com.example.demo.controller;

import com.example.demo.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/send-mail")
    public ResponseEntity<String> sendMail() {
        mailService.sendEmail(
                "duy@gmail.com",
                "Subject Info",
                "Content Info"
        );
        return ResponseEntity.ok("Send mail successfully!");
    }
}
