package com.myblog.controller;

import com.myblog.model.Comment;
import com.myblog.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CommentControllerTest {
    @Mock
    private CommentService commentService;
    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

    }
    @Test
    void addComment_ShouldCreateComment(){
        String Resualt = commentController.addComment(1L,"comment");

        verify(commentService).createComment(argThat(comment -> comment.getText().equals("comment") && comment.getPostId().equals(1L)));

        assertEquals("redirect:/posts/1", Resualt);
    }
    @Test
    void deleteComment_ShouldCallServiceAndRedirect() {

        String result = commentController.deleteComment(1L, 5L);

        verify(commentService).deleteById(5L);
        assertEquals("redirect:/posts/1", result);
    }
    @Test
    void updateComment_ShouldUpdateTextAndRedirect() {
        // Arrange
        Comment existingComment = new Comment(5L,"Old text", LocalDateTime.now(),1L);
        when(commentService.getById(5L)).thenReturn(Optional.of(existingComment));

        // Act
        String result = commentController.updateComment(1L, 5L, "text");

        // Assert
        assertEquals("text", existingComment.getText()); // Проверка обновления текста
        verify(commentService).updateComment(existingComment); // Используем existingComment
        assertEquals("redirect:/posts/1", result);
    }
}
