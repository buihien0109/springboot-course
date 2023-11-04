package com.example.demomockmvctest.controller;

import com.example.demomockmvctest.model.Post;
import com.example.demomockmvctest.response.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j // Logger
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPost() throws Exception {
        // Call API
        MvcResult result = mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Get response
        String response = result.getResponse().getContentAsString();

        // Convert response to List<Post>
        List<Post> posts = objectMapper.readValue(response, new TypeReference<>() {
        });
        log.info("posts: {}", posts);

        // Verify
        Assertions.assertNotNull(posts);
        Assertions.assertEquals(3, posts.size());
    }

    @Test
    void getPostById() throws Exception {
        // Call API
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Get response
        String response = result.getResponse().getContentAsString();

        // Convert response to Post
        Post post = objectMapper.readValue(response, Post.class);

        // Verify
        Assertions.assertNotNull(post);
        Assertions.assertEquals(1, post.getId());
    }

    @Test
    void getPostById_1() throws Exception {
        // Call API throw exception
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts/100"))
//                .andExpect(status().isNotFound());

        // Call API throw exception with message
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts/100"))
//                .andExpect(status().isNotFound())
//                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));

        // Call API throw exception and verify message
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts/100"))
//                .andExpect(status().isNotFound())
//                .andExpect(result -> {
//                    log.info("result: {}", result);
//                    Assertions.assertNotNull(result.getResolvedException());
//                    Assertions.assertEquals("Not found post", result.getResolvedException().getMessage());
//                });

        // Call API throw exception and verify body response
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    log.info("result: {}", result);
                    Assertions.assertNotNull(result.getResolvedException());
                    Assertions.assertEquals("Not found post", result.getResolvedException().getMessage());

                    String response = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
                    log.info("errorResponse: {}", errorResponse);

                    Assertions.assertEquals("Not found post", errorResponse.getMessage());
                    Assertions.assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatus());
                });
    }

    @Test
    public void testCreatePost() throws Exception {
        Post newPost = new Post();
        newPost.setTitle("New Post");
        newPost.setAuthor("John Doe");

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newPost)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    Post post = objectMapper.readValue(response, Post.class);
                    log.info("post: {}", post);

                    Assertions.assertNotNull(post);
                    Assertions.assertEquals("New Post", post.getTitle());
                    Assertions.assertEquals("John Doe", post.getAuthor());
                });
    }

    @Test
    public void testUpdatePost() throws Exception {
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Post");
        updatedPost.setAuthor("Jane Doe");

        mockMvc.perform(MockMvcRequestBuilders.put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedPost)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    Post post = objectMapper.readValue(response, Post.class);

                    Assertions.assertNotNull(post);
                    Assertions.assertEquals("Updated Post", post.getTitle());
                    Assertions.assertEquals("Jane Doe", post.getAuthor());
                });
    }

    @Test
    public void testDeletePost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchPost() throws Exception {
        String keyword = "Post";
        // Call API
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/search").param("title", keyword))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    List<Post> posts = objectMapper.readValue(response, new TypeReference<>() {
                    });
                    log.info("posts: {}", posts);

                    Assertions.assertNotNull(posts);
                    Assertions.assertEquals(3, posts.size());
                });
    }

    // Helper method to convert an object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}