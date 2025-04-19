package com.myblog.controller;

import com.myblog.DTO.PostPageResponse;
import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostViewControllerTest {

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
    void testShowPosts_DefaultParams() throws Exception {
        Post post = new Post(1L, "Test Title", "image.jpg", "Content", "tag1,tag2", 0, LocalDateTime.now());
        postRepository.save(post);

        mockMvc.perform(get("/myblog/posts")
                        .param("pageSize", "10")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"));
    }

    @Test
    void testViewPost_ValidId() throws Exception {
        Post savedPost = postRepository.save(new Post(
                1L, "Single Post", "img.png", "Full content", "java", 5, LocalDateTime.now()));

        mockMvc.perform(get("/myblog/posts/{id}", savedPost.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("post"));
    }

    @Test
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/myblog/posts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"));
    }
}