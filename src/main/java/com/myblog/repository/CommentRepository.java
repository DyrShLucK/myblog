package com.myblog.repository;

import com.myblog.model.Comment;
import com.myblog.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository{
    Comment save(Comment comment);
    List<Comment> findAllByPostId(Long post_id);
    void deleteById(Long id);
    Optional<Comment> findById(Long id);
    void updateCommentText(Long id, String text);
}