package com.myblog.service;

import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTag(tag);
    }

    public void incrementPostLikes(Long postId) {
        postRepository.incrementLikes(postId);
    }

    public Optional<Integer> getLikesCount(Long postId) {
        return postRepository.getLikesCount(postId);
    }
}