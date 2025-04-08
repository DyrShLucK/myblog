package com.myblog.repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.myblog.model.Post;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update(
                "INSERT INTO post (title, image, text, tags, likes_count) VALUES (?, ?, ?, ?, ?)",
                post.getTitle(), post.getImage(), post.getText(), post.getTags(), 0
        );
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM post",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getString("text"),
                        rs.getString("tags"),
                        rs.getInt("likes_count")
                )
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(
                "DELETE FROM post WHERE id = ?",
                id
        );
    }
    @Override
    public void incrementLikes(Long postId) {
        jdbcTemplate.update(
                "UPDATE post SET likes_count = likes_count + 1 WHERE id = ?",
                postId
        );
    }
    public Optional<Integer> getLikesCount(Long postId) {
        try {
            Integer result = jdbcTemplate.queryForObject(
                    "SELECT likes_count FROM post WHERE id = ?",
                    new Object[]{postId},
                    Integer.class
            );
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
