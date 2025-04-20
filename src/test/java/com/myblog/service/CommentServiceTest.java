package com.myblog.service;

import com.myblog.model.Comment;
import com.myblog.model.Post;
import com.myblog.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class CommentServiceTest {
    //Используем обычные Unit тесты без поднятия SpringBoot
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testCreateComment() {
        Comment inputComment = new Comment("Test comment", 1L);
        Comment savedComment = new Comment(1L, "Test comment", LocalDateTime.now(), 1L);

        when(commentRepository.save(inputComment)).thenReturn(savedComment);

        Comment result = commentService.createComment(inputComment);

        assertNotNull(result.getId());
        assertEquals("Test comment", result.getText());
        assertEquals(1L, result.getPostId());
        verify(commentRepository).save(inputComment);
    }

    @Test
    void testGetAllCommentByPostId() {
        Long postId = 1L;
        List<Comment> mockComments = Arrays.asList(
                new Comment(1L, "Comment 1", LocalDateTime.now(), postId),
                new Comment(2L, "Comment 2", LocalDateTime.now(), postId)
        );

        when(commentRepository.findAllByPostId(postId)).thenReturn(mockComments);

        List<Comment> result = commentService.getAllCommentByPostId(postId);

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(postId, result.get(0).getPostId());
        verify(commentRepository).findAllByPostId(postId);
    }

    @Test
    void testDeleteById() {
        commentService.deleteById(1L);
        verify(commentRepository).deleteById(1L);
    }

    @Test
    void testCountCommentsByPosts() {
        Post post1 = new Post(1L, "Title 1", "image.jpg","Content 1","tags", 0, LocalDateTime.now());
        Post post2 = new Post(2L, "Title 2","image.jpg", "Content 2","tags", 0, LocalDateTime.now());
        List<Post> posts = List.of(post1, post2);

        Comment comment1 = new Comment(1L, "Text 1",LocalDateTime.now(), 1L);
        Comment comment2 = new Comment(2L, "Text 2",LocalDateTime.now(), 1L);
        Comment comment3 = new Comment(3L, "Text 3",LocalDateTime.now(), 2L);

        when(commentRepository.findAllByPostId(1L))
                .thenReturn(List.of(comment1, comment2));
        when(commentRepository.findAllByPostId(2L))
                .thenReturn(List.of(comment3));

        Map<Long, List<Comment>> result = commentService.countCommentsByPosts(posts);

        assertEquals(2, result.size());
        assertEquals(2, result.get(1L).size());
        assertEquals(1, result.get(2L).size());
        verify(commentRepository, times(2)).findAllByPostId(anyLong());
    }
}