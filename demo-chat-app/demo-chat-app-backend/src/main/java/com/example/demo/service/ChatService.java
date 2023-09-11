package com.example.demo.service;

import com.example.demo.request.LoginRequest;
import com.example.demo.request.RegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {
    @Value("${chat.project_id}")
    private String projectId;

    @Value("${chat.private_key}")
    private String privateKey;

    public Map<String, Object> login(LoginRequest request) throws JsonProcessingException {
        String API_URL = "https://api.chatengine.io/users/me";
        RestTemplate restTemplate = new RestTemplate();

        // Header config
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("Project-ID", projectId);
        headers.set("User-Name", request.getName());
        headers.set("User-Secret", request.getSecret());

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send a GET request with the custom headers
        ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        // Retrieve the response body
        String responseBody = responseEntity.getBody();

        // Covert response to Map
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};

        return objectMapper.readValue(responseBody, typeReference);
    }

    public Map<String, Object> register(RegisterRequest request) throws JsonProcessingException {
        String API_URL = "https://api.chatengine.io/users";
        RestTemplate restTemplate = new RestTemplate();

        // Header config
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("Private-Key", privateKey);

        // Request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", request.getName());
        requestBody.put("secret", request.getSecret());
        requestBody.put("email", request.getEmail());
        requestBody.put("first_name", request.getFirstName());
        requestBody.put("last_name", request.getLastName());

        // Create an HttpEntity with the headers
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Send a GET request with the custom headers
        ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        // Retrieve the response body
        String responseBody = responseEntity.getBody();

        // Covert response to Map
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};

        return objectMapper.readValue(responseBody, typeReference);
    }
}
