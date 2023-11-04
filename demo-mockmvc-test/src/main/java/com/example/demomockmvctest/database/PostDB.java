package com.example.demomockmvctest.database;

import com.example.demomockmvctest.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDB {
    public static List<Post> postList = new ArrayList<>(List.of(
            new Post(1, "Post 1", "Author 1"),
            new Post(2, "Post 2", "Author 2"),
            new Post(3, "Post 3", "Author 3")
    ));
}
