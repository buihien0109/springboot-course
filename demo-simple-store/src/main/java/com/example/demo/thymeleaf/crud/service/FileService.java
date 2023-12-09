package com.example.demo.thymeleaf.crud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class FileService {
    private String uploadDir = "uploads";

    public FileService() {
        try {
            createUploadDir();
        } catch (IOException e) {
            log.error("Could not create upload dir");
        }
    }

    public void createUploadDir() throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    public String saveFile(MultipartFile multipartFile) throws IOException {
        // file name is miliseconds
        String fileName = String.valueOf(System.currentTimeMillis());

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/" + uploadDir + "/" + fileName;
        } catch (IOException e) {
            throw new IOException("Could not save image file: " + fileName, e);
        }
    }
}
