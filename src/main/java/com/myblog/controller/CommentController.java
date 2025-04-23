package com.myblog.controller;

import com.myblog.model.Comment;
import com.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("myblog/")
public class CommentController {
    @Autowired
    CommentService commentService;

    //Добавление комментария
    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable Long postId,
                             @RequestParam("text") String text) {
        Comment comment = new Comment(text, postId);
        commentService.createComment(comment);
        return "redirect:/myblog/posts/" + postId;
    }

    // Удаление комментария
    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId) {
        commentService.deleteById(commentId);
        return "redirect:/myblog/posts/" + postId;
    }

    // Редактирование комментария
    @PostMapping("/posts/{postId}/comments/{commentId}")
    public String updateComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @RequestParam("text") String newText) {
        Comment comment = commentService.getById(commentId);
        comment.setText(newText);
        commentService.updateComment(comment);
        return "redirect:/myblog/posts/" + postId;
    }
}
