package com.myblog.controller;

import com.myblog.model.Post;
import com.myblog.service.CommentService;
import com.myblog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostViewControllerTest {

    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;
    @Mock
    private Model model;

    @InjectMocks
    private PostViewController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowPosts_DefaultParams() {
        // Arrange
        Post post = new Post(1L, "Test", "img.jpg", "Content","tags",0,LocalDateTime.now());
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        when(postService.getAllPosts()).thenReturn(posts);

        // Act
        String view = controller.showPosts("", 10, 1, model);

        // Assert
        assertEquals("posts", view);
        verify(postService).getAllPosts();
        verify(model).addAttribute(eq("posts"), anyList());
    }

    @Test
    void testViewPost_ValidId() {
        // Arrange
        Post post = new Post(1L, "Test", "img.jpg", "Content","tags",0,LocalDateTime.now());
        when(postService.getPostById(1L)).thenReturn(Optional.of(post));

        // Act
        String view = controller.viewPost(1L, model);

        // Assert
        assertEquals("post", view);
        verify(model).addAttribute("post", post);
    }

    @Test
    void testShowAddForm() {
        // Act
        String view = controller.showAddForm(model);

        // Assert
        assertEquals("add-post", view);
    }
}