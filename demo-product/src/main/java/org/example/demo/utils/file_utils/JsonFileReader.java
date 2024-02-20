package org.example.demo.utils.file_utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.demo.model.Product;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Component
public class JsonFileReader implements IFileReader {
    @Override
    public List<Product> readFile(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Read the JSON file using java.nio.file.Path
            Path jsonFilePath = Paths.get(filePath);
            byte[] jsonData = Files.readAllBytes(jsonFilePath);

            // Convert JSON to List of Post objects
            return objectMapper.readValue(jsonData, new TypeReference<List<Product>>() {
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }
}
