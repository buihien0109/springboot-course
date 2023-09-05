package com.example.demo.controller;

import com.example.demo.request.MailComplexRequest;
import com.example.demo.request.MailRequest;
import com.example.demo.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/send-mail-simple")
    public ResponseEntity<?> sendMailSimple(@RequestBody MailRequest request) {
        mailService.sendMailSimple(request);
        return ResponseEntity.ok("Send mail successfully");
    }

    @PostMapping("/send-mail-with-attachment")
    public ResponseEntity<?> sendMailWithAttachment(@RequestBody MailRequest request) {
        mailService.sendMailWithAttachment(request);
        return ResponseEntity.ok("Send mail successfully");
    }

    @PostMapping("/send-mail-with-template")
    public ResponseEntity<?> sendMailWithTemplate(@RequestBody MailRequest request) {
        mailService.sendMailWithTemplate(request);
        return ResponseEntity.ok("Send mail successfully");
    }

    @PostMapping("/send-mail-with-template-thymeleaf")
    public ResponseEntity<?> sendMailWithTemplateThymeleaf(@RequestBody MailComplexRequest request) {
        mailService.sendMailWithTemplateThymeleaf(request);
        return ResponseEntity.ok("Send mail successfully");
    }
}
