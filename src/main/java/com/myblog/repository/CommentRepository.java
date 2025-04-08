package com.myblog.repository;

import com.myblog.model.Comment;
import com.myblog.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository{
    Comment save(Comment comment);
    List<Comment> findAllByPostId(Long post_id);
    void deleteById(Long id);
}