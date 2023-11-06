package com.example.demo.course.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file);

    byte[] readFile(String id);
}
