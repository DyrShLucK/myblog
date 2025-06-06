package com.myblog.repository;

import com.myblog.model.Post;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface PostRepository{
    Post save(Post post);
    List<Post> findAllWithPagnation(int pageSize, int pageNumber);
    List<Post> findAllWithPagnationAndTag(int pageSize, int pageNumber, String tag);
    List<Post> findAll();
    void deleteById(Long id);
    void incrementLikes(Long postId);
    void decrementLikes(Long postId);
    Optional<Integer> getLikesCount(Long postId);
    List<Post> findByTag(String tag);
    Optional<Post> findById(Long id);
    void UpdatePost(Post post);
    int getTotalPostsCount();
    int getTotalPostsCountByTag(String tag);
}