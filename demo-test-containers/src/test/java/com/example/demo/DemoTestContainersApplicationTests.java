package com.example.demo;

import com.example.demo.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class DemoTestContainersApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//    @Test
//    public void testGetProductById() throws Exception {
//        // Tạo một sản phẩm để kiểm tra
//        Product product = new Product();
//        product.setName("Test Product");
//        product.setPrice(19.99);
//
//        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(product)))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        String content = createResult.getResponse().getContentAsString();
//        Long productId = objectMapper.readValue(content, Product.class).getId();
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testCreateProduct() throws Exception {
//        Product product = new Product();
//        product.setName("Test Product");
//        product.setPrice(19.99);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(product)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    public void testUpdateProduct() throws Exception {
//        Product product = new Product();
//        product.setName("Test Product");
//        product.setPrice(19.99);
//
//        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(product)))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        String content = createResult.getResponse().getContentAsString();
//        Long productId = objectMapper.readValue(content, Product.class).getId();
//
//        Product updatedProduct = new Product();
//        updatedProduct.setName("Updated Product");
//        updatedProduct.setPrice(29.99);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/" + productId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedProduct)))
//                .andExpect(status().isOk());
//
//        // Kiểm tra xem sản phẩm đã được cập nhật thành công
//        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String updatedContent = getResult.getResponse().getContentAsString();
//        Product updatedResult = objectMapper.readValue(updatedContent, Product.class);
//
//        assertEquals(updatedResult.getName(), "Updated Product");
//        assertEquals(updatedResult.getPrice(), 29.99);
//    }
//
//    @Test
//    public void testDeleteProduct() throws Exception {
//        Product product = new Product();
//        product.setName("Test Product");
//        product.setPrice(19.99);
//
//        MvcResult createResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(product)))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        String content = createResult.getResponse().getContentAsString();
//        Long productId = objectMapper.readValue(content, Product.class).getId();
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        // Kiểm tra xem sản phẩm đã bị xóa
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
}
