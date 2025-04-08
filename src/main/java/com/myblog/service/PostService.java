package com.myblog.service;


import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return (List<Post>) postRepository.findAll();
    }
}