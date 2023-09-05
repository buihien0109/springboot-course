package com.example.demo.service;

import com.example.demo.request.MailComplexRequest;
import com.example.demo.request.MailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@AllArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;
    private final TemplateEngine templateEngine;

    // 1. Gửi email với text
    public void sendMailSimple(MailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getText());

        javaMailSender.send(message);
    }

    // 2. Gửi email với file đính kèm
    public void sendMailWithAttachment(MailRequest request) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getSubject());

            // Attach the file
            Resource resource = resourceLoader.getResource("classpath:static/LinuxNotesForProfessionals.pdf");
            FileSystemResource file = new FileSystemResource(resource.getFile());
            helper.addAttachment(file.getFilename(), file);

            javaMailSender.send(message);
        } catch (IOException | MessagingException e) {
            System.out.println("Error sending email : " + e.getMessage());
        }
    }

    // 3. Gửi email với template HTML
    public void sendMailWithTemplate(MailRequest request) {
        try {
            // Load template
            String templatePath = "classpath:templates/template-mail.html";
            String templateData = loadTemplateContent(templatePath);

            // Replace placeholders with dynamic content
            templateData = templateData.replace("${username}", request.getText());

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(templateData, true); // Enable HTML content

            javaMailSender.send(message);
        } catch (IOException | MessagingException e) {
            System.out.println("Error sending email : " + e.getMessage());
        }
    }

    // Helper function : Load template
    private String loadTemplateContent(String templatePath) throws IOException {
        Path path = resourceLoader.getResource(templatePath).getFile().toPath();
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    // 4. Gửi email với template HTML sử dụng Thymeleaf
    public void sendMailWithTemplateThymeleaf(MailComplexRequest request) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());

            // Create the Thymeleaf context
            Context context = new Context();
            context.setVariable("username", request.getUsername());
            context.setVariable("email", request.getEmail());
            context.setVariable("age", request.getAge());
            context.setVariable("city", request.getCity());
            context.setVariable("country", request.getCountry());

            // Use the template engine to process the template
            String htmlContent = templateEngine.process("template-mail-mutil-data", context);
            helper.setText(htmlContent, true); // Enable HTML content

            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("Error sending email : " + e.getMessage());
        }
    }
}
