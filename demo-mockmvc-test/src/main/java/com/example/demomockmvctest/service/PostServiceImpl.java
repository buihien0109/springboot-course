package com.example.demomockmvctest.service;

import org.springframework.stereotype.Service;
import com.example.demomockmvctest.dao.PostDAO;
import com.example.demomockmvctest.exception.BadRequestException;
import com.example.demomockmvctest.exception.ResouceNotFoundException;
import com.example.demomockmvctest.model.Post;

import java.util.List;
import java.util.Random;

@Service
public class PostServiceImpl implements PostService {
    private final PostDAO postDAO;

    public PostServiceImpl(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    @Override
    public List<Post> getAllPost() {
        return postDAO.findAll();
    }

    @Override
    public Post getPostById(Integer id) {
        List<Post> postList = postDAO.findAll();
        return postList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResouceNotFoundException("Not found post"));
    }

    @Override
    public Post createPost(Post request) {
        Random rd = new Random();
        Post post = new Post();
        post.setId(rd.nextInt(1000));
        post.setTitle(request.getTitle());
        post.setAuthor(request.getAuthor());

        postDAO.save(post);
        return post;
    }

    @Override
    public Post updatePost(Integer id, Post request) {
        Post post = getPostById(id);
        post.setTitle(request.getTitle());
        post.setAuthor(request.getAuthor());
        return post;
    }

    @Override
    public void deletePost(Integer id) {
        Post post = getPostById(id);
        postDAO.delete(id);
    }

    @Override
    public List<Post> searchPost(String title) {
        if(title.trim().isEmpty()) {
            throw new BadRequestException("Title không được để trống");
        }
        return postDAO.findByTitleContainsIgnoreCase(title);
    }
}
