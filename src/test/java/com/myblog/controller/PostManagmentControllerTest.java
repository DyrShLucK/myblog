package com.myblog.controller;

import com.myblog.controller.PostManagmentController;
import com.myblog.model.Post;
import com.myblog.service.ImageStorageService;
import com.myblog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
public class PostManagmentControllerTest {

    @Mock
    private PostService postService;
    @Mock
    private ImageStorageService imageStorageService;
    @Mock
    private Model model;

    @InjectMocks
    private PostManagmentController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPost_ShouldCreatePost() {
        // Arrange
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "content".getBytes());
        Post mockPost = new Post();
        when(postService.createPost(any())).thenReturn(mockPost);
        when(imageStorageService.savePostImage(eq(mockPost.getId()), any())).thenReturn("images/test.jpg");

        // Act
        String result = controller.addPost("Title", "Text", "tags", image);

        // Assert
        verify(postService).createPost(any());
        verify(imageStorageService).savePostImage(eq(mockPost.getId()), eq(image));
        verify(postService).updatePost(any());
        assertEquals("redirect:/posts", result);
    }

    @Test
    void likePost_ShouldIncrementLikes() {
        // Act
        String result = controller.LikeControll(1L, true);

        // Assert
        verify(postService).incrementPostLikes(1L);
        assertEquals("redirect:/posts/1", result);
    }

    @Test
    void deletePost_ShouldCallService() {
        // Act
        String result = controller.DeletePost(1L);

        // Assert
        verify(postService).deletePost(1L);
        assertEquals("redirect:/posts", result);
    }

    @Test
    void editPost_ShouldReturnFormView() {
        // Arrange
        Post post = new Post(1L, "Test", "image.jpg", "Content", "tags", 0, null);
        when(postService.getPostById(1L)).thenReturn(Optional.of(post));

        // Act
        String result = controller.EditPost(1L, model);

        // Assert
        verify(postService).getPostById(1L);
        verify(model).addAttribute("post", post);
        assertEquals("add-post", result);
    }

    @Test
    void updatePost_ShouldUpdatePost() {
        // Arrange
        Post existingPost = new Post(1L, "Old", "old.jpg", "Content", "tags", 0, null);
        when(postService.getPostById(1L)).thenReturn(Optional.of(existingPost));
        MockMultipartFile image = new MockMultipartFile("image", "new.jpg", "image/jpeg", "content".getBytes());

        // Act
        String result = controller.updatePost(1L, "New Title", "New Text", "new tags", image);

        // Assert
        verify(postService).getPostById(1L);
        verify(imageStorageService).updatePostImage(eq(1L), eq(image), eq("old.jpg"));
        verify(postService).updatePost(any());
        assertEquals("redirect:/posts/1", result);
    }
}
