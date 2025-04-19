package com.myblog.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Table("post")
public class Post {
    @Id
    private Long id;
    private String title;
    private String image;
    private String text;
    private String tags;
    private int likesCount;
    private LocalDateTime created_at;
    // Конструктор для создания из БД
    public Post(Long id, String title, String image, String text, String tags, int likesCount, LocalDateTime created_at) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.text = text;
        this.tags = tags;
        this.likesCount = likesCount;
        this.created_at = created_at;
    }

    // Конструктор для нового поста (без id)
    public Post(String title, String image, String text, String tags) {
        this.title = title;
        this.image = image;
        this.text = text;
        this.tags = tags;
        this.likesCount = 0;
    }

    public Post(){};

    public Long getId() {
        return id;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setId(long l) {
        this.id = l;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getPreviewText() {
        if (text == null || text.isEmpty()) return "";
        String[] lines = text.split("\n", -1);
        int maxLines = 3;
        if (lines.length <= maxLines) {
            return String.join("\n", lines);
        } else {
            return String.join("\n", Arrays.copyOfRange(lines, 0, maxLines)) + "...";
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", text='" + text + '\'' +
                ", tags='" + tags + '\'' +
                ", likesCount=" + likesCount +
                ", created_at=" + created_at +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id.equals(post.id) &&
                title.equals(post.title) &&
                image.equals(post.image) &&
                text.equals(post.text) &&
                tags.equals(post.tags) &&
                likesCount == post.likesCount &&
                created_at.equals(post.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, image, text, tags, likesCount, created_at);
    }
}