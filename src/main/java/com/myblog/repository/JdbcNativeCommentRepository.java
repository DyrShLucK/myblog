package com.myblog.repository;

import com.myblog.model.Comment;
import com.myblog.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment save(Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO comment (text, post_id) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, comment.getText());
            ps.setLong(2, comment.getPostId());
            return ps;
        }, keyHolder);


        Number generatedId = (Number) keyHolder.getKeys().get("id");
        comment.setId(generatedId.longValue());
        return comment;
    }
    @Override
    public List<Comment> findAllByPostId(Long post_id){
        return jdbcTemplate.query(
                "SELECT id, text, created_at, post_id FROM comment WHERE post_id = ?",
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getLong("post_id")
                ),
                post_id
                );
    }
    @Override
    public void deleteById(Long id){
        jdbcTemplate.update(
                "Delete from comment where id = ?",
                id
        );
    }
}
