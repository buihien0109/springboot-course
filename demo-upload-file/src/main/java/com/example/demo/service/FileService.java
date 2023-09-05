package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FileService {
    private final String uploadDir = System.getProperty("user.dir")
            .concat(File.separator)
            .concat("uploads");

    public FileService() {
        createFolder(uploadDir);
    }

    public void createFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public String uploadFile(MultipartFile file) {
        // Tạo path tương ứng với file Upload lên
        String fileId = UUID.randomUUID().toString();
        File serverFile = new File(uploadDir.concat(File.separator).concat(fileId));

        try {
            file.transferTo(serverFile);
            return "/api/files/".concat(fileId);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload file");
        }
    }

    public byte[] readFile(String id) {
        // Kiểm tra đường dẫn file có tồn tại hay không
        File file = new File(uploadDir.concat(File.separator).concat(id));
        if (!file.exists()) {
            throw new RuntimeException("Không thể đọc file : " + id);
        }

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file : " + id);
        }
    }
}
