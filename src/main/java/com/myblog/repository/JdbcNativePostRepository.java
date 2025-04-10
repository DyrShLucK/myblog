package com.myblog.repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.myblog.model.Post;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post save(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO post (title, image, text, tags, likes_count) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getImage());
            ps.setString(3, post.getText());
            ps.setString(4, post.getTags());
            ps.setInt(5, 0);
            return ps;
        }, keyHolder);

        Number generatedId = (Number) keyHolder.getKeys().get("id");
        post.setId(generatedId.longValue());
        Timestamp generatedTime = (Timestamp) keyHolder.getKeys().get("created_at");
        post.setCreated_at(generatedTime.toLocalDateTime());
        return post;
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
                        rs.getInt("likes_count"),
                        rs.getTimestamp("created_at").toLocalDateTime()
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
    @Override
    public List<Post> findByTag(String tag) {
        String sql = "SELECT * FROM post WHERE tags LIKE ?";
        return jdbcTemplate.query(sql,
                new Object[]{"%" + tag + "%"},
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getString("text"),
                        rs.getString("tags"),
                        rs.getInt("likes_count"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
    }
    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";
        try {
            Post post = jdbcTemplate.queryForObject(sql, new Object[]{id},
                    (rs, rowNum) -> new Post(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("image"),
                            rs.getString("text"),
                            rs.getString("tags"),
                            rs.getInt("likes_count"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
            return Optional.ofNullable(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
