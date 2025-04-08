package com.myblog.repository;

import com.myblog.model.Post;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface PostRepository{
    void save(Post post);
    List<Post> findAll();
    void deleteById(Long id);
    void incrementLikes(Long postId);
    Optional getLikesCount(Long postId);
}