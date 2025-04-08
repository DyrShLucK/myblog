package com.myblog.repository;
import com.myblog.configuration.DatabaseConfiguration;
import com.myblog.model.Post;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DatabaseConfiguration.class, JdbcNativePostRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
class JdbcNativePostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM post");
        jdbcTemplate.execute("ALTER TABLE post ALTER COLUMN id RESTART WITH 1"); // Для H2
        jdbcTemplate.execute("DELETE FROM comment"); // Если есть зависимые таблицы

        jdbcTemplate.update(
                "INSERT INTO post (title, image, text, tags) VALUES (?, ?, ?, ?)",
                "Заголовок 1", "image1.jpg", "Текст поста 1", "новости, спорт"
        );
        jdbcTemplate.update(
                "INSERT INTO post (title, image, text, tags) VALUES (?, ?, ?, ?)",
                "Заголовок 2", "image2.jpg", "Текст поста 2", "технологии"
        );
    }

    @Test
    void save_shouldAddPostToDatabase() {
        Post newPost = new Post(
                "Новый пост",
                "new.jpg",
                "Текст нового поста",
                "тестирование"
        );

        postRepository.save(newPost);

        List<Post> posts = postRepository.findAll();
        Post savedPost = posts.stream()
                .filter(p -> "Новый пост".equals(p.getTitle()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedPost);
        assertNotNull(savedPost.getId());
        assertEquals("тестирование", savedPost.getTags());
    }

    @Test
    void findAll_shouldReturnAllPosts() {
        List<Post> posts = postRepository.findAll();
        assertNotNull(posts);
        assertEquals(2, posts.size());
    }

    @Test
    void deleteById_shouldRemovePostFromDatabase() {
        postRepository.deleteById(1L);
        List<Post> posts = postRepository.findAll();

        assertEquals(1, posts.size());
    }
    @Test
    void incrementLikes_shouldIncreaseLikesCount() {
        Long postId = 1L;
        Optional<Integer> initialLikes = postRepository.getLikesCount(postId);
        assertTrue(initialLikes.isPresent());
        int initialCount = initialLikes.get();
        postRepository.incrementLikes(postId);
        Optional<Integer> updatedLikes = postRepository.getLikesCount(postId);
        assertTrue(updatedLikes.isPresent());
        assertEquals(initialCount + 1, updatedLikes.get().intValue());
    }

    @Test
    void getLikesCount_shouldReturnCorrectCount() {
        // Arrange
        Long postId = 1L;

        // Act
        Optional<Integer> likes = postRepository.getLikesCount(postId);

        // Assert
        assertTrue(likes.isPresent());
        assertEquals(0, likes.get().intValue()); // По умолчанию 0 в тестовых данных
    }

    @Test
    void getLikesCount_shouldReturnEmptyForNonExistingPost() {
        // Arrange
        Long nonExistingId = 999L;

        // Act
        Optional<Integer> likes = postRepository.getLikesCount(nonExistingId);

        // Assert
        assertTrue(likes.isEmpty());
    }
}
