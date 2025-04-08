package com.myblog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table("post")
public class Post {
    private Long id;
    private String title;
    private String image;
    private String text;
    private String tags;
    private int likesCount;

    // Конструктор для создания из БД
    public Post(Long id, String title, String image, String text, String tags, int likesCount) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.text = text;
        this.tags = tags;
        this.likesCount = likesCount;
    }

    // Конструктор для нового поста (без id)
    public Post(String title, String image, String text, String tags) {
        this.title = title;
        this.image = image;
        this.text = text;
        this.tags = tags;
        this.likesCount = 0;
    }

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

}