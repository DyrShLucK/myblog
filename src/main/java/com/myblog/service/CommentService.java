package com.myblog.service;

import com.myblog.model.Comment;
import com.myblog.repository.CommentRepository;

import java.util.List;

public class CommentService {
    private final CommentRepository commentRepository;


    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment){
        return commentRepository.save(comment);
    }
    public List<Comment> getAllCommentByPostId(Long post_id){
        return commentRepository.findAllByPostId(post_id);
    }
    public void deleteById(Long id){
        commentRepository.deleteById(id);
    }

}
