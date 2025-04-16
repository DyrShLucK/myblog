package com.myblog.repository;

import com.myblog.model.Post;
import com.myblog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JdbcNativePostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comment");
        jdbcTemplate.execute("DELETE FROM post");
        jdbcTemplate.execute("ALTER TABLE post ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE comment ALTER COLUMN id RESTART WITH 1");



        jdbcTemplate.update(
                "INSERT INTO post (title, image, text, tags) VALUES (?, ?, ?, ?)",
                "Заголовок 1", "image1.jpg", "Текст поста 1", "новости, спорт"
        );
        jdbcTemplate.update(
                "INSERT INTO post (title, image, text, tags) VALUES (?, ?, ?, ?)",
                "Заголовок 2", "image2.jpg", "Текст поста 2", "технологии"
        );
        jdbcTemplate.update(
                "INSERT INTO post (title, image, text, tags) VALUES (?, ?, ?, ?)",
                "Заголовок 3", "image3.jpg", "Текст поста 3", "новости"
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

        Post SavedPost = postRepository.save(newPost);

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
        assertEquals(3, posts.size());
    }

    @Test
    void deleteById_shouldRemovePostFromDatabase() {
        postRepository.deleteById(1L);
        List<Post> posts = postRepository.findAll();

        assertEquals(2, posts.size());
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
    @Test
    void getPostById_shouldReturnPost(){
        Optional<Post> post = postRepository.findById(1L);
        assertTrue(post.isPresent());
        Post GetedPost = post.get();
        assertEquals("Заголовок 1", GetedPost.getTitle());
    }
    @Test
    void findByTag_ShouldReturnPostList(){
        List<Post> posts = postRepository.findByTag("новости");
        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertEquals("Текст поста 1", posts.get(0).getText());
        assertEquals("Текст поста 3", posts.get(1).getText());
    }
    @Test
    void UpdatePost_shouldUpdatePost(){
        Post post = postRepository.findById(1L).get();
        post.setText("New Text about...");
        postRepository.UpdatePost(post);
        assertEquals("New Text about...", postRepository.findById(1L).get().getText());
    }
    @Test
    void testFindAllWithPagination() {
        // Act
        List<Post> postsPage1 = postRepository.findAllWithPagnation(2, 1);
        List<Post> postsPage2 = postRepository.findAllWithPagnation(2, 2);

        // Assert
        assertEquals(2, postsPage1.size());
        assertEquals("Заголовок 3", postsPage1.get(0).getTitle());
        assertEquals("Заголовок 2", postsPage1.get(1).getTitle());

        assertEquals(1, postsPage2.size());
        assertEquals("Заголовок 1", postsPage2.get(0).getTitle());
    }

    @Test
    void testFindAllWithPaginationAndTag() {
        // Act
        List<Post> newsPostsPage1 = postRepository.findAllWithPagnationAndTag(1, 1, "новости");
        List<Post> newsPostsPage2 = postRepository.findAllWithPagnationAndTag(1, 2, "новости");
        List<Post> techPosts = postRepository.findAllWithPagnationAndTag(10, 1, "технологии");

        // Assert
        assertEquals(1, newsPostsPage1.size());
        assertEquals("Заголовок 3", newsPostsPage1.get(0).getTitle());

        assertEquals(1, newsPostsPage2.size());
        assertEquals("Заголовок 1", newsPostsPage2.get(0).getTitle());

        assertEquals(1, techPosts.size());
        assertEquals("Заголовок 2", techPosts.get(0).getTitle());
    }
}
