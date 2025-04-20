package com.myblog.controller;

import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostManagmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM post");
    }

    @Test
    void testAddPost() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test data".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/myblog/posts")
                        .file(image)
                        .param("title", "New Post")
                        .param("text", "Post content")
                        .param("tags", "spring,test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myblog/posts"));

        List<Post> posts = postRepository.findAll();
        assertEquals(1, posts.size());
        assertEquals("New Post", posts.get(0).getTitle());
        assertEquals("Post content", posts.get(0).getText());
    }

    @Test
    void testDeletePost() throws Exception {
        Post post = postRepository.save(new Post(
                1L, "ToDelete", "image.jpg", "Content", "delete", 0, LocalDateTime.now()
        ));

        mockMvc.perform(MockMvcRequestBuilders.post("/myblog/posts/{postId}/delete", post.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myblog/posts"));

        assertFalse(postRepository.findById(post.getId()).isPresent());
    }

    @Test
    void testLikePost() throws Exception {
        Post post = postRepository.save(new Post(
                1L, "Test Post", "image.jpg", "Content", "java", 0, LocalDateTime.now()
        ));

        // Выполняем запрос на лайк
        mockMvc.perform(MockMvcRequestBuilders.post("/myblog/posts/{postId}/like", post.getId())
                        .param("like", "true"))
                .andExpect(redirectedUrl("/myblog/posts/" + post.getId()));

        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals(1, updatedPost.getLikesCount());
    }

    @Test
    void testEditPost() throws Exception {
        Post post = postRepository.save(new Post(
                1L, "Old Title", "image.jpg", "Old Content", "java", 0, LocalDateTime.now()
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/myblog/posts/{postId}/edit", post.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attribute("post", post));
    }

    @Test
    void testUpdatePost() throws Exception {
        Post post = postRepository.save(new Post(
                1L, "Original", "image.jpg", "Content", "java", 0, LocalDateTime.now()
        ));

        MockMultipartFile image = new MockMultipartFile(
                "image", "update.jpg", MediaType.IMAGE_JPEG_VALUE, "new data".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/myblog/posts/{postId}", post.getId())
                        .file(image)
                        .param("title", "Updated Title")
                        .param("text", "New Content")
                        .param("tags", "spring,updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myblog/posts/" + post.getId()));

        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("New Content", updatedPost.getText());
        assertEquals("spring,updated", updatedPost.getTags());
    }
}