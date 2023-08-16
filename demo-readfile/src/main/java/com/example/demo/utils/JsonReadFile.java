package com.example.demo.utils;

import com.example.demo.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component("JsonReadFile")
public class JsonReadFile implements ReadFile {
    // ResourceLoader là một interface cung cấp các phương thức để load tài nguyên trong ứng dụng. Interface này được sử dụng để đọc file hoặc data từ các nguồn khác nhau, chẳng hạn như classpath, file system, URL, và nhiều nguồn khác.
    private final ResourceLoader resourceLoader;

    public JsonReadFile(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<User> readFile(String filePath) {
        // Tạo List rỗng để chứa dữ liệu
        List<User> userList = new ArrayList<>();
        try {
            // Load file
            Resource resource = resourceLoader.getResource(filePath);

            // Đọc dữ liệu từ file
            String jsonContent = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

            // Sử dụng thư viện Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            userList = objectMapper.readValue(jsonContent, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
