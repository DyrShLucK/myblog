package com.myblog.service;

import com.myblog.excepetion.CommentNotFoundExcepetion;
import com.myblog.excepetion.PostNotFoundException;
import com.myblog.model.Comment;
import com.myblog.model.Post;
import com.myblog.repository.CommentRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Comment getById(Long id){
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundExcepetion(id));
    }
    public void updateComment(Comment comment){
        commentRepository.updateComment(comment);
    }
    public Map<Long, List<Comment>> countCommentsByPosts(List<Post> posts){
        Map<Long, List<Comment>> commentsMap = new HashMap<>();
        for (Post post : posts) {
            List<Comment> comments = commentRepository.findAllByPostId(post.getId());
            commentsMap.put(post.getId(), comments);
        }
        return commentsMap;
    }
}
