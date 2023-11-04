package com.example.demo.user.management.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<String> getFiles(Integer userId);

    String uploadFile(Integer userId, MultipartFile file);

    byte[] readFile(Integer userId, String fileName);

    void deleteFile(Integer userId, String fileName);

    void deleteAllFiles(Integer userId);
}
