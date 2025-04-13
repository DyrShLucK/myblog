package com.myblog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("comment")
public class Comment {
    private Long id;
    private String text;
    private LocalDateTime created_at;
    private Long postId; // Внешний ключ

    // Конструкторы, геттеры и сеттеры
    public Comment(Long id, String text, LocalDateTime createdAt, Long postId) {
        this.id = id;
        this.text = text;
        this.created_at = createdAt;
        this.postId = postId;
    }

    // Конструктор для нового комментария (без id и createdAt)
    public Comment(String text, Long postId) {
        this.text = text;
        this.postId = postId;
        this.created_at = LocalDateTime.now();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }
    public Long getPostId() {
        return postId;
    }
}