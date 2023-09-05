package com.example.demo.controller;

import com.example.demo.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Upload file
    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) {
        String filePath = fileService.uploadFile(file);
        return ResponseEntity.ok(filePath);
    }

    // Xem thông tin file
    @GetMapping("/{id}")
    public ResponseEntity<?> readFile(@PathVariable String id) {
        byte[] data = fileService.readFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(data);
    }
}
