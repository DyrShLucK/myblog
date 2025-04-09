package com.myblog.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void testCreatePost() {
        // Arrange
        Post post = new Post("New Title", "image.jpg", "Content", "java");
        Post savedPost = new Post(1L, "New Title", "image.jpg", "Content", "java", 0, LocalDateTime.now());

        when(postRepository.save(post)).thenReturn(savedPost);

        // Act
        Post result = postService.createPost(post);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(postRepository).save(post);
    }

    @Test
    void testDeletePost() {
        // Act
        postService.deletePost(1L);

        // Assert
        verify(postRepository).deleteById(1L);
    }

    @Test
    void testGetAllPosts() {
        // Arrange
        List<Post> mockPosts = Arrays.asList(
                new Post(1L, "Post1", "img1.jpg", "Text1", "java", 5, LocalDateTime.now()),
                new Post(2L, "Post2", "img2.jpg", "Text2", "spring", 3, LocalDateTime.now())
        );

        when(postRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<Post> result = postService.getAllPosts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Post1", result.get(0).getTitle());
        verify(postRepository).findAll();
    }

    @Test
    void testGetPostsByTag() {
        // Arrange
        List<Post> mockPosts = Arrays.asList(
                new Post(1L, "Spring Post", "img.jpg", "Content", "spring,java", 10, LocalDateTime.now())
        );

        when(postRepository.findByTag("spring")).thenReturn(mockPosts);

        // Act
        List<Post> result = postService.getPostsByTag("spring");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals("Spring Post", result.get(0).getTitle());
        verify(postRepository).findByTag("spring");
    }

    @Test
    void testIncrementPostLikes() {
        // Act
        postService.incrementPostLikes(1L);

        // Assert
        verify(postRepository).incrementLikes(1L);
    }

    @Test
    void testGetLikesCount() {
        // Arrange
        when(postRepository.getLikesCount(1L)).thenReturn(Optional.of(42));

        // Act
        Optional<Integer> likes = postService.getLikesCount(1L);

        // Assert
        assertTrue(likes.isPresent());
        assertEquals(42, likes.get());
        verify(postRepository).getLikesCount(1L);
    }

    @Test
    void testGetLikesCountNotFound() {
        // Arrange
        when(postRepository.getLikesCount(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Integer> likes = postService.getLikesCount(999L);

        // Assert
        assertTrue(likes.isEmpty());
        verify(postRepository).getLikesCount(999L);
    }
}