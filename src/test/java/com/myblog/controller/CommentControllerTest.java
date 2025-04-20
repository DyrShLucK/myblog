package com.myblog.controller;

import com.myblog.controller.CommentController;
import com.myblog.model.Comment;
import com.myblog.model.Post;
import com.myblog.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        post = new Post(1L, "Test Post", "image.jpg", "Content", "java", 0, LocalDateTime.now());
        comment = new Comment(1L, "Original comment", LocalDateTime.now(), post.getId());
    }

    @Test
    void addComment_ShouldCreateCommentAndRedirect() throws Exception {
        mockMvc.perform(post("/myblog/posts/{postId}/comments", post.getId())
                        .param("text", "New comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myblog/posts/" + post.getId()));

        verify(commentService, times(1)).createComment(any(Comment.class));
    }

    @Test
    void updateComment_ShouldUpdateCommentAndRedirect() throws Exception {
        when(commentService.getById(comment.getId())).thenReturn(Optional.of(comment));

        mockMvc.perform(post("/myblog/posts/{postId}/comments/{commentId}", post.getId(), comment.getId())
                        .param("text", "Updated comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myblog/posts/" + post.getId()));

        verify(commentService).updateComment(argThat(updatedComment ->
                updatedComment.getText().equals("Updated comment") &&
                        updatedComment.getId().equals(comment.getId())
        ));
    }

    @Test
    void deleteComment_ShouldDeleteCommentAndRedirect() throws Exception {
        mockMvc.perform(post("/myblog/posts/{postId}/comments/{commentId}/delete",
                        post.getId(), comment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myblog/posts/" + post.getId()));

        verify(commentService, times(1)).deleteById(comment.getId());
    }
}