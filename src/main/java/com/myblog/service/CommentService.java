package com.myblog.service;

import com.myblog.model.Comment;
import com.myblog.model.Post;
import com.myblog.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
    public Optional<Comment> getById(Long id){
        return commentRepository.findById(id);
    }
    public void updateCommentText(Long id, String text){
        commentRepository.updateCommentText(id, text);
    }
}
