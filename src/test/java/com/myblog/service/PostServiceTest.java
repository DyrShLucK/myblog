package com.myblog.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post("Test Post", "img.jpg", "Content", "testTag" );
    }


    @Test
    void testCreatePost_GeneratesId() {

        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            savedPost.setId(1L);
            return savedPost;
        });

        Post createdPost = postService.createPost(new Post("Test Post2", "img2.jpg", "Content2", "testTag2" ));

        assertNotNull(createdPost.getId());
        assertEquals(1L, createdPost.getId());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testDeletePost_CallsRepository() {
        postService.deletePost(1L);
        verify(postRepository).deleteById(1L);
    }

    @Test
    void testGetAllPosts_ReturnsRepoResult() {
        List<Post> mockPosts = List.of(post);
        when(postRepository.findAll()).thenReturn(mockPosts);

        List<Post> result = postService.getAllPosts();

        assertEquals(mockPosts, result);
        verify(postRepository).findAll();
    }

    @Test
    void testIncrementLikes_CallsRepository() {
        postService.incrementPostLikes(1L);
        verify(postRepository).incrementLikes(1L);
    }

    @Test
    void testGetLikesCount_ReturnsRepoResult() {
        when(postRepository.getLikesCount(1L)).thenReturn(Optional.of(5));
        when(postRepository.getLikesCount(999L)).thenReturn(Optional.empty());

        assertEquals(Optional.of(5), postService.getLikesCount(1L));
        assertEquals(Optional.empty(), postService.getLikesCount(999L));

        verify(postRepository).getLikesCount(1L);
        verify(postRepository).getLikesCount(999L);
    }
}