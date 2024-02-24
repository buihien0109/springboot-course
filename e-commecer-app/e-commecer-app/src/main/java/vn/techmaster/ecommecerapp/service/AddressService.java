package vn.techmaster.ecommecerapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.techmaster.ecommecerapp.model.request.LoginRequest;

import java.util.Map;

@Service
@Slf4j
public class AddressService {
    @Value("${spring.api.ghtk.token}")
    private String tokenApi;

    public Map<String, Object> getAllProvince() throws JsonProcessingException {
        log.info("Get all province");

        String API_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
        RestTemplate restTemplate = new RestTemplate();

        // Header config
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("Token", tokenApi);

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

    public Map<String, Object> getAllDistrictByProvince(Integer provinceId) throws JsonProcessingException {
        log.info("Get all district by province id: " + provinceId);

        String API_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=" + provinceId;
        RestTemplate restTemplate = new RestTemplate();

        // Header config
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("Token", tokenApi);

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

    public Map<String, Object> getAllWardByDistrict(Integer districtId) throws JsonProcessingException {
        log.info("Get all ward by district id: " + districtId);

        String API_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtId;
        RestTemplate restTemplate = new RestTemplate();

        // Header config
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("Token", tokenApi);

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
}
