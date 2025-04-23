package com.myblog.excepetion;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long postId) {
        super("Пост с ID " + postId + " не найден");
    }
}
