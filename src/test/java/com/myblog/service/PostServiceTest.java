package com.myblog.service;

// Добавьте эти импорты в начало теста
import com.myblog.configuration.DatabaseConfiguration;
import com.myblog.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class, PostService.class})
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    public void testGetAllPosts() {
        List<Post> posts = postService.getAllPosts();
        assertThat(posts).hasSize(1);
    }
}