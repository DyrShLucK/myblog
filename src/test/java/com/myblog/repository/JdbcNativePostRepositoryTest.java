package com.myblog.repository;

import com.myblog.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcNativePostRepository.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class JdbcNativePostRepositoryTest {

    @Autowired
    private JdbcNativePostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Post testPost;

    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setTitle("Test Title");
        testPost.setImage("test.jpg");
        testPost.setText("Test content");
        testPost.setTags("java,spring");
    }

    @Test
    @DirtiesContext
    void save_ShouldPersistPostWithGeneratedIdAndTimestamp() {
        // Act
        Post savedPost = postRepository.save(testPost);

        // Assert
        assertThat(savedPost.getId()).isPositive();
        assertThat(savedPost.getCreated_at()).isNotNull();

        // Assert
        Optional<Post> foundPost = postRepository.findById(savedPost.getId());
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo("Test Title");
    }

    @Test
    void findAllWithPagination_ShouldReturnPostsInDescendingOrder() {
        // Arrange
        Post post1 = createTestPost("Post 1", "tag1");
        Post post2 = createTestPost("Post 2", "tag2");
        Post post3 = createTestPost("Post 3", "tag3");

        // Act
        List<Post> postsPage1 = postRepository.findAllWithPagnation(2, 1);
        List<Post> postsPage2 = postRepository.findAllWithPagnation(2, 2);

        // Assert
        assertThat(postsPage1).hasSize(2);
        assertThat(postsPage1.get(0).getTitle()).isEqualTo("Post 3");
        assertThat(postsPage1.get(1).getTitle()).isEqualTo("Post 2");

        assertThat(postsPage2).hasSize(2);
        assertThat(postsPage2.get(0).getTitle()).isEqualTo("Post 1");
    }

    @Test
    void findByTag_ShouldReturnFilteredPosts() {
        // Arrange
        createTestPost("Java Post", "java");
        createTestPost("Spring Post", "spring");
        createTestPost("Database Post", "database");

        // Act
        List<Post> javaPosts = postRepository.findByTag("java");
        List<Post> springPosts = postRepository.findByTag("spring");

        // Assert
        assertThat(javaPosts).hasSize(1);
        assertThat(javaPosts.get(0).getTitle()).isEqualTo("Java Post");

        assertThat(springPosts).hasSize(1);
        assertThat(springPosts.get(0).getTitle()).isEqualTo("Spring Post");
    }

    @Test
    @DirtiesContext
    void incrementLikes_ShouldIncreaseLikeCount() {
        // Arrange
        Post post = createTestPost("Like Test", "test");
        int initialLikes = post.getLikesCount();

        // Act
        postRepository.incrementLikes(post.getId());
        Optional<Integer> updatedLikes = postRepository.getLikesCount(post.getId());

        // Assert
        assertThat(updatedLikes).hasValue(initialLikes + 1);
    }

    @Test
    @DirtiesContext
    void updatePost_ShouldModifyPostFields() {
        // Arrange
        Post post = createTestPost("Original Title", "old_tag");
        post.setTitle("Updated Title");
        post.setTags("new_tag");

        // Act
        postRepository.UpdatePost(post);
        Optional<Post> updatedPost = postRepository.findById(post.getId());

        // Assert
        assertThat(updatedPost).isPresent();
        assertThat(updatedPost.get().getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.get().getTags()).isEqualTo("new_tag");
    }

    @Test
    @DirtiesContext
    void deleteById_ShouldRemovePost() {
        // Arrange
        Post post = createTestPost("To Delete", "delete");

        // Act
        postRepository.deleteById(post.getId());
        Optional<Post> deletedPost = postRepository.findById(post.getId());

        // Assert
        assertThat(deletedPost).isEmpty();
    }

    private Post createTestPost(String title, String tags) {
        Post post = new Post();
        post.setTitle(title);
        post.setTags(tags);
        post.setImage("test.jpg");
        post.setText("Test content");
        return postRepository.save(post);
    }
}