package com.myblog.service;

import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PostServiceTest {
    //Используем обычные Unit тесты без поднятия SpringBoot
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void testCreatePost() {
        Post post = new Post("New Title", "image.jpg", "Content", "java");
        Post savedPost = new Post(1L, "New Title", "image.jpg", "Content", "java", 0, LocalDateTime.now());

        when(postRepository.save(post)).thenReturn(savedPost);

        Post result = postService.createPost(post);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(postRepository).save(post);
    }

    @Test
    void testDeletePost() {
        postService.deletePost(1L);
        verify(postRepository).deleteById(1L);
    }

    @Test
    void testGetAllPosts() {
        List<Post> mockPosts = Arrays.asList(
                new Post(1L, "Post1", "img1.jpg", "Text1", "java", 5, LocalDateTime.now()),
                new Post(2L, "Post2", "img2.jpg", "Text2", "spring", 3, LocalDateTime.now())
        );

        when(postRepository.findAll()).thenReturn(mockPosts);

        List<Post> result = postService.getAllPosts();

        assertEquals(2, result.size());
        assertEquals("Post1", result.get(0).getTitle());
        verify(postRepository).findAll();
    }

    @Test
    void testGetPostsByTag() {
        List<Post> mockPosts = Arrays.asList(
                new Post(1L, "Spring Post", "img.jpg", "Content", "spring,java", 10, LocalDateTime.now())
        );

        when(postRepository.findByTag("spring")).thenReturn(mockPosts);

        List<Post> result = postService.getPostsByTag("spring");

        assertFalse(result.isEmpty());
        assertEquals("Spring Post", result.get(0).getTitle());
        verify(postRepository).findByTag("spring");
    }

    @Test
    void testIncrementPostLikes() {
        postService.incrementPostLikes(1L);
        verify(postRepository).incrementLikes(1L);
    }

    @Test
    void testGetLikesCount() {
        when(postRepository.getLikesCount(1L)).thenReturn(Optional.of(42));

        Optional<Integer> likes = postService.getLikesCount(1L);

        assertTrue(likes.isPresent());
        assertEquals(42, likes.get());
        verify(postRepository).getLikesCount(1L);
    }

    @Test
    void testGetLikesCountNotFound() {
        when(postRepository.getLikesCount(999L)).thenReturn(Optional.empty());

        Optional<Integer> likes = postService.getLikesCount(999L);

        assertTrue(likes.isEmpty());
        verify(postRepository).getLikesCount(999L);
    }
}