package com.example.demomockmvctest.dao;

import com.example.demomockmvctest.model.Post;

import java.util.List;

public interface PostDAO {
    List<Post> findAll();

    void save(Post post);

    void delete(Integer id);

    List<Post> findByTitleContainsIgnoreCase(String title);
}
