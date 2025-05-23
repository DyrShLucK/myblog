package com.myblog.repository;

import com.myblog.model.Comment;
import com.myblog.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(JdbcNativeCommentRepository.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class JdbcNativeCommentRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comment");
        jdbcTemplate.execute("DELETE FROM post");
        jdbcTemplate.execute("ALTER TABLE post ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE comment ALTER COLUMN id RESTART WITH 1"); // Добавлено

        // Создаем тестовый пост
        jdbcTemplate.update(
                "INSERT INTO post (title, text) VALUES (?, ?)",
                "Test Post", "Post content"
        );
    }

    @Test
    @DirtiesContext
    void save_shouldAddCommentToDatabase() {
        Comment newComment = new Comment(
                "Test comment",
                1L
        );

        Comment savedComment = commentRepository.save(newComment);

        assertNotNull(savedComment.getId());
        assertEquals("Test comment", savedComment.getText());
        assertEquals(1L, savedComment.getPostId());

        // Проверка через JDBC
        List<Comment> comments = jdbcTemplate.query(
                "SELECT * FROM comment",
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getLong("post_id")
                )
        );

        assertEquals(1, comments.size());
        assertEquals("Test comment", comments.get(0).getText());
    }

    @Test
    void findAllByPostId_shouldReturnCommentsForPost() {
        // Создаем два комментария для поста 1
        jdbcTemplate.update(
                "INSERT INTO comment (text, post_id) VALUES (?, ?)",
                "Comment 1", 1L
        );
        jdbcTemplate.update(
                "INSERT INTO comment (text, post_id) VALUES (?, ?)",
                "Comment 2", 1L
        );

        List<Comment> comments = commentRepository.findAllByPostId(1L);

        assertEquals(2, comments.size());
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Comment 1")));
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Comment 2")));
    }

    @Test
    @DirtiesContext
    void deleteById_shouldRemoveCommentFromDatabase() {
        // Создаем комментарий
        jdbcTemplate.update(
                "INSERT INTO comment (text, post_id) VALUES (?, ?)",
                "To delete", 1L
        );

        // Получаем его id
        Long idToDelete = jdbcTemplate.queryForObject(
                "SELECT id FROM comment WHERE text = 'To delete'",
                Long.class
        );
        commentRepository.deleteById(idToDelete);
        List<Comment> comments = commentRepository.findAllByPostId(1L);
        assertTrue(comments.isEmpty());
    }

    @Test
    @DirtiesContext
    void save_shouldGenerateCreatedAtAutomatically() {
        Comment newComment = new Comment("Test comment", 1L);
        Comment savedComment = commentRepository.save(newComment);

        LocalDateTime dbCreatedAt = jdbcTemplate.queryForObject(
                "SELECT created_at FROM comment WHERE id = ?",
                LocalDateTime.class,
                savedComment.getId()
        );
        assertNotNull(dbCreatedAt);
        assertTrue(dbCreatedAt.isBefore(LocalDateTime.now().plusSeconds(1)));
    }
    @Test
    @DirtiesContext
    void updateComment_ShouldUpdateCommentInDB(){
        Comment comment = new Comment("Text", 1L);
        comment = commentRepository.save(comment);
        comment.setText("New Text");
        commentRepository.updateComment(comment);
    }
}