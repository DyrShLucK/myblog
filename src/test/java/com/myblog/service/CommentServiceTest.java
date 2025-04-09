package com.myblog.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.myblog.model.Comment;
import com.myblog.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void testCreateComment() {
        // Arrange
        Comment inputComment = new Comment("Test comment", 1L);
        Comment savedComment = new Comment(1L, "Test comment", LocalDateTime.now(), 1L);

        when(commentRepository.save(inputComment)).thenReturn(savedComment);

        // Act
        Comment result = commentService.createComment(inputComment);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Test comment", result.getText());
        assertEquals(1L, result.getPostId());
        verify(commentRepository).save(inputComment);
    }

    @Test
    void testGetAllCommentByPostId() {
        // Arrange
        Long postId = 1L;
        List<Comment> mockComments = Arrays.asList(
                new Comment(1L, "Comment 1", LocalDateTime.now(), postId),
                new Comment(2L, "Comment 2", LocalDateTime.now(), postId)
        );

        when(commentRepository.findAllByPostId(postId)).thenReturn(mockComments);

        // Act
        List<Comment> result = commentService.getAllCommentByPostId(postId);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(postId, result.get(0).getPostId());
        verify(commentRepository).findAllByPostId(postId);
    }

    @Test
    void testDeleteById() {
        // Act
        commentService.deleteById(1L);

        // Assert
        verify(commentRepository).deleteById(1L);
    }
}