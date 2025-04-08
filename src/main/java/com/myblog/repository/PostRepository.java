package com.myblog.repository;

import com.myblog.model.Post;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PostRepository{
    void save(Post post);
    List<Post> findAll();
    void deleteById(Long id);
}