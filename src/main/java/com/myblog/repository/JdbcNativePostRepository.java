package com.myblog.repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.myblog.model.Post;

import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update(
                "INSERT INTO post (title, image, text, tags) VALUES (?, ?, ?, ?)",
                post.getTitle(), post.getImage(), post.getText(), post.getTags()
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
                        rs.getString("tags")
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
}
