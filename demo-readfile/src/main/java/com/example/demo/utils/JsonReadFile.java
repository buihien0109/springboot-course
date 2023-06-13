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
    private final ResourceLoader resourceLoader;

    public JsonReadFile(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<User> readFile(String filePath) {
        List<User> userList = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource(filePath);
            String jsonContent = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            userList = objectMapper.readValue(jsonContent, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
